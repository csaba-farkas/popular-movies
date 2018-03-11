package com.csabafarkas.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    @BindView(R.id.iv) ImageView imageView;
    @BindView(R.id.tv) TextView textView;
    MovieDbService movieDbService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
//        Picasso.with(this)
//                .load("http://i.imgur.com/DvpvklR.png")
//                .placeholder(R.drawable.ic_icon_img_placeholder)
//                .into(imageView);
        NetworkUtils.getMostPopularMovies(BuildConfig.MovieDbApiKey, 1, this);
    }

    @Override
    public void onSuccess(List<Movie> movies) {
        String s = "";
        for (Movie movie : movies) {
            s += movie.getTitle();
        }
        textView.setText(s);
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
