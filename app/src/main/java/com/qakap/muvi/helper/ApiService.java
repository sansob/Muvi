package com.qakap.muvi.helper;

import com.qakap.muvi.models.Movie;
import com.qakap.muvi.models.Result;
import com.qakap.muvi.models.genre.GenreResponse;
import com.qakap.muvi.models.review.ReviewResponse;
import com.qakap.muvi.models.trailer.TrailerResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import static com.qakap.muvi.config.Config.BASE_API;

public interface ApiService {
    @GET("movie/now_playing")
    Call<Movie> GetNowPlaying(
            @Query("api_key") String apiKey,
            @Query("language") String lang,
            @Query("page") int pageNumber
    );

    @GET("movie/{movie_id}?api_key="+BASE_API+"&language=en-US")
    Call<Result> getMovieDetail(
            @Path("movie_id") String movie_id
    );


    @GET("genre/movie/list")
    Call<GenreResponse> getGenres(
            @Query("api_key") String apiKey,
            @Query("language") String lang
    );

    @GET("movie/{movie_id}/reviews?api_key="+BASE_API+"&language=en-US")
    Call<ReviewResponse> getMovieReviews(
            @Path("movie_id") String movie_id
    );



    @GET("movie/{movie_id}/videos?api_key="+BASE_API+"&language=en-US")
    Call<TrailerResponse> getMovieTrailers(
            @Path("movie_id") String movie_id
    );


}
