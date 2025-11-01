package com.example.demokullu.data.remote

import com.example.demokullu.data.remote.dto.NewsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    // Normal posts
    @GET("posts")
    suspend fun getNews(
        @Query("limit") limit: Int = 10,
        @Query("skip") skip: Int = 0
    ): NewsResponse

    // ✅ Actual search API (different endpoint)
    @GET("posts/search")
    suspend fun searchNews(
        @Query("q") query: String,
        @Query("limit") limit: Int = 10,
        @Query("skip") skip: Int = 0
    ): NewsResponse
}
