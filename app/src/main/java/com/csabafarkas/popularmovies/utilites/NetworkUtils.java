package com.csabafarkas.popularmovies.utilites;

import com.csabafarkas.popularmovies.BuildConfig;

/**
 * Created by lastminute84 on 10/03/18.
 */

public final class NetworkUtils {

    private static final String BASE_URL = "https://api.themoviedb.org/";
    private static final String API_KEY = BuildConfig.MovieDbApiKey;

    public static MovieDbService getMovieDbService() {
        return RetrofitClient.getClient(BASE_URL).create(MovieDbService.class);
    }

}
