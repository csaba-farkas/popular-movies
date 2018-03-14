package com.csabafarkas.popularmovies.utilites;

import com.csabafarkas.popularmovies.BuildConfig;
import com.csabafarkas.popularmovies.models.Movie;
import com.csabafarkas.popularmovies.models.MovieCollection;
import com.csabafarkas.popularmovies.models.RetrofitError;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by lastminute84 on 10/03/18.
 */

public final class NetworkUtils {

    private enum SortType {
        MOST_POPULAR,
        TOP_RATED
    }

    private static final String BASE_URL = "https://api.themoviedb.org/";
    private static final String API_KEY = BuildConfig.MovieDbApiKey;

    public static MovieDbService getMovieDbService() {
        return RetrofitClient.getClient(BASE_URL).create(MovieDbService.class);
    }

    public static void getMostPopularMovies(String apiKey, int pageNumber, final MovieCallback callback) {
        fetchMovies(apiKey, pageNumber, callback, SortType.MOST_POPULAR);
    }

    public static void getTopRatedMovies(String apiKey, int pageNumber, final MovieCallback callback) {
        fetchMovies(apiKey, pageNumber, callback, SortType.TOP_RATED);
    }

    private static void fetchMovies(String apiKey, int pageNumber, final MovieCallback callback, SortType sortType) {
        MovieDbService movieDbService = getMovieDbService();

        switch (sortType) {
            case TOP_RATED:
                movieDbService.getTopRatedMovies(apiKey, pageNumber)
                        .enqueue(new Callback<MovieCollection>() {
                            @Override
                            public void onResponse(Call<MovieCollection> call, retrofit2.Response<MovieCollection> response) {

                                if (response.isSuccessful()) {
                                    callback.onSuccess(response.body().getMovies());
                                } else {
                                    Gson gson = new Gson();
                                    RetrofitError error = gson.fromJson(response.errorBody().charStream(), RetrofitError.class);
                                    callback.onFailure(error);
                                }
                            }

                            @Override
                            public void onFailure(Call<MovieCollection> call, Throwable t) {
                                callback.onError(t);
                            }
                        });
                break;
            case MOST_POPULAR:
                movieDbService.getMostPopularMovies(apiKey, pageNumber)
                        .enqueue(new Callback<MovieCollection>() {
                            @Override
                            public void onResponse(Call<MovieCollection> call, retrofit2.Response<MovieCollection> response) {

                                if (response.isSuccessful()) {
                                    callback.onSuccess(response.body().getMovies());
                                } else {
                                    Gson gson = new Gson();
                                    RetrofitError error = gson.fromJson(response.errorBody().charStream(), RetrofitError.class);
                                    callback.onFailure(error);
                                }
                            }

                            @Override
                            public void onFailure(Call<MovieCollection> call, Throwable t) {
                                callback.onError(t);
                            }
                        });
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

}
