package com.csabafarkas.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.csabafarkas.popularmovies.adapters.MovieAdapter;
import com.csabafarkas.popularmovies.models.Movie;
import com.csabafarkas.popularmovies.models.MovieCollection;
import com.csabafarkas.popularmovies.models.RetrofitError;
import com.csabafarkas.popularmovies.utilites.MovieCallback;
import com.csabafarkas.popularmovies.utilites.MovieDbService;
import com.csabafarkas.popularmovies.utilites.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity implements MovieCallback {

    @BindView(R.id.activity_main_root_gv)
    GridView rootView;
    int pageNumber = 1;
    List<Movie> movieCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // load more on scroll to bottom
        rootView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // no need to implement
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                final int lastItem = firstVisibleItem + visibleItemCount;
                if (lastItem == totalItemCount) {
                    NetworkUtils.getMostPopularMovies(BuildConfig.MovieDbApiKey, pageNumber++, MainActivity.this);
                }
            }
        });
        NetworkUtils.getMostPopularMovies(BuildConfig.MovieDbApiKey, pageNumber, this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("page_number", pageNumber);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.containsKey("page_number")) {
            pageNumber = savedInstanceState.getInt("page_number");
        }
    }

    @Override
    public void onSuccess(List<Movie> movies) {
        if (movieCollection == null) {
            movieCollection = movies;
        } else {
            movieCollection.addAll(movies);
        }

        if (rootView.getAdapter() == null) {
            MovieAdapter movieAdapter = new MovieAdapter(this, 0, movieCollection);
            rootView.setAdapter(movieAdapter);
        } else {
            ((MovieAdapter) rootView.getAdapter()).notifyDataSetChanged();
        }
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
