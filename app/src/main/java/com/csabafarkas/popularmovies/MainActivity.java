package com.csabafarkas.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.csabafarkas.popularmovies.models.Movie;
import com.csabafarkas.popularmovies.models.MovieCollection;
import com.csabafarkas.popularmovies.utilites.MovieDbService;
import com.csabafarkas.popularmovies.utilites.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Response;
import retrofit2.Call;
import retrofit2.Callback;

public class MainActivity extends AppCompatActivity {

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
        movieDbService = NetworkUtils.getMovieDbService();
        movieDbService.getMostPopularMovies(BuildConfig.MovieDbApiKey, 1)
                .enqueue(new Callback<MovieCollection>() {
                    @Override
                    public void onResponse(Call<MovieCollection> call, retrofit2.Response<MovieCollection> response) {

                        if (response.isSuccessful()) {
                            textView.setText(response.raw().toString());
                        } else {
                            textView.setText("Call failure - status code: " + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<MovieCollection> call, Throwable t) {
                        textView.setText("on failure bazmeg");
                    }
                });
    }
}
