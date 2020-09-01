package com.mardous.newsletter.api;

import com.mardous.newsletter.api.model.News;
import com.mardous.newsletter.api.model.Sources;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("top-headlines")
    Call<News> getTopHeadlines(@Query("country") String country,
                               @Query("category") String category,
                               @Query("pageSize") int pageSize,
                               @Query("apiKey") String apiKey);

    @GET("top-headlines")
    Call<News> getTopHeadlinesFromSource(@Query("sources") String sources,
                                         @Query("pageSize") int pageSize,
                                         @Query("apiKey") String apiKey);

    @GET("everything")
    Call<News> getNewsSearch(@Query("qInTitle") String keyword,
                             @Query("language") String language,
                             @Query("sortBy") String sortBy,
                             @Query("pageSize") int pageSize,
                             @Query("apiKey") String apiKey);

    @GET("everything")
    Call<News> getNewsSearch(@Query("qInTitle") String keyword,
                             @Query("language") String language,
                             @Query("excludeDomains") String excludeDomains,
                             @Query("sortBy") String sortBy,
                             @Query("pageSize") int pageSize,
                             @Query("apiKey") String apiKey);

    @GET("sources")
    Call<Sources> getSources(@Query("language") String language, @Query("apiKey") String apiKey);
}
