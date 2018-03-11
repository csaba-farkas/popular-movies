package com.csabafarkas.popularmovies.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.csabafarkas.popularmovies.models.Movie;

import java.util.List;

/**
 * Created by lastminute84 on 11/03/18.
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    public MovieAdapter(@NonNull Context context, int resource, @NonNull List<Movie> movies) {
        super(context, resource, movies);
    }
}
