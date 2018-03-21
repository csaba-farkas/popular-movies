package com.csabafarkas.popularmovies.utilites;

import com.csabafarkas.popularmovies.models.Movie;
import com.csabafarkas.popularmovies.models.MovieCollection;
import com.csabafarkas.popularmovies.models.RetrofitError;

import java.util.List;

/**
 * Created by lastminute84 on 11/03/18.
 */

public interface MovieCollectionCallback {
    void onSuccess(MovieCollection movieCollection);
    void onFailure(RetrofitError error);
    void onError(Throwable t);
}
