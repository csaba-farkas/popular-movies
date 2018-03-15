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
import timber.log.Timber;

public class MainActivity extends AppCompatActivity implements MovieCallback {

    @BindView(R.id.activity_main_root_gv)
    GridView rootView;
    int pageNumber;
    List<Movie> movieCollection;
    boolean userScrolled;
    boolean loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Timber.plant(new Timber.DebugTree());
        Timber.tag("MoviePager");
        Timber.d("Timber was planted. pageNumber: " + pageNumber);

        // load more on scroll to bottom
        rootView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    userScrolled = true;
                    return;
                }
                userScrolled = false;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (userScrolled && firstVisibleItem + visibleItemCount == totalItemCount && !loading) {
                    Timber.d("in onScroll - totalItemcount: " + totalItemCount + ", pageNumber: " + pageNumber);
                    NetworkUtils.getMostPopularMovies(BuildConfig.MovieDbApiKey, pageNumber++, MainActivity.this);
                    loading = true;
                }
            }
        });

        if (pageNumber == 0) pageNumber = 1;
        NetworkUtils.getMostPopularMovies(BuildConfig.MovieDbApiKey, pageNumber, this);
        Timber.d("onCreate finished! pageNubmber: " + pageNumber);
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
        } else {
            pageNumber = 1;
        }
    }

    @Override
    public void onSuccess(List<Movie> movies) {
        Timber.d("Adding new movies. Page number: %s", pageNumber);
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
