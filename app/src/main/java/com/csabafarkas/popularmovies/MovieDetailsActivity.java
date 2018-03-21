package com.csabafarkas.popularmovies;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailsActivity extends AppCompatActivity {

    @BindView(R.id.movie_details_poster_iv)
    ImageView posterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        ButterKnife.bind(this);

        if (getIntent() != null) {
            if (getIntent().hasExtra(getResources().getString(R.string.poster_url))) {
                Picasso.with(this)
                        .load(String.format(getResources().getString(R.string.poster_base_url, getIntent().getStringExtra(getResources().getString(R.string.poster_url)))))
                        .placeholder(R.drawable.ic_icon_img_placeholder)
                        .into(posterImageView);
            }
        }
    }
}
