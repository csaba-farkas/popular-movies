package com.csabafarkas.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.csabafarkas.popularmovies.adapters.MovieAdapter;
import com.csabafarkas.popularmovies.models.Movie;
import com.csabafarkas.popularmovies.models.MovieCollection;
import com.csabafarkas.popularmovies.models.RetrofitError;
import com.csabafarkas.popularmovies.utilites.MovieCollectionCallback;
import com.csabafarkas.popularmovies.utilites.NetworkUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieCollectionCallback {

    @BindView(R.id.activity_main_root_gv)
    GridView rootView;
    int pageNumber;
    int currentPosition;
    List<Movie> movies;
    boolean loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        if (movies == null) {
            loading = true;
            pageNumber++;
            NetworkUtils.getMostPopularMovies(BuildConfig.MovieDbApiKey, pageNumber, this);
        }

        // load more on scroll to bottom
        rootView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount && !loading) {
                    pageNumber++;
                    NetworkUtils.getMostPopularMovies(BuildConfig.MovieDbApiKey, pageNumber, MainActivity.this);
                    loading = true;
                }
            }
        });

        // add onItemClickListener to GridView
        rootView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                intent.putExtra(getResources().getString(R.string.poster_url), movies.get(position).getPosterPath());
                startActivity(intent);
            }
        });

        // scroll to current position
        if (currentPosition >= 0)
            rootView.smoothScrollToPosition(currentPosition);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("page_number", pageNumber); // save last page loaded
        int cp = rootView.getFirstVisiblePosition();
        outState.putInt("current_position", rootView.getFirstVisiblePosition());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("page_number")) {
            pageNumber = savedInstanceState.getInt("page_number");
        }
        if (savedInstanceState.containsKey("current_position")) {
            currentPosition = savedInstanceState.getInt("current_position");
        }
    }

    @Override
    public void onSuccess(MovieCollection movieCollection) {
        pageNumber = movieCollection.getPage();
        if (movies == null) {
            movies = movieCollection.getMovies();
        } else {
            this.movies.addAll(movieCollection.getMovies());
        }

        if (rootView.getAdapter() == null) {
            MovieAdapter movieAdapter = new MovieAdapter(this, 0, movies);
            rootView.setAdapter(movieAdapter);
        } else {
            ((MovieAdapter) rootView.getAdapter()).notifyDataSetChanged();
        }
        loading = false;
    }

    @Override
    public void onFailure(RetrofitError retrofitError) {
        Toast.makeText(this, retrofitError.getErrorCode() + " " + retrofitError.getErrorMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onError(Throwable t) {
        Toast.makeText(this, t.getMessage(), Toast.LENGTH_LONG).show();

    }
}
