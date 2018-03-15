package com.csabafarkas.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.csabafarkas.popularmovies.R;
import com.csabafarkas.popularmovies.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by lastminute84 on 11/03/18.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    private final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w500";

    public MovieAdapter(@NonNull Context context, int resource, @NonNull List<Movie> movies) {
        super(context, resource, movies);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_item_movie, parent, false);
        }

        ImageView posterImageView = convertView.findViewById(R.id.poster_iv);
        Picasso.with(getContext())
            .load(POSTER_BASE_URL + movie.getPosterPath())
            .placeholder(R.drawable.ic_icon_img_placeholder)
            .into(posterImageView);

        // debug
        TextView debugTv = convertView.findViewById(R.id.debug_tv);
        debugTv.setText("" + position);
        return convertView;
    }
}
