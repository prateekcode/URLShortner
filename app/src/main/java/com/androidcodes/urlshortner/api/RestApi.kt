package com.androidcodes.urlshortner.api

import com.androidcodes.urlshortner.model.UrlModel
import com.androidcodes.urlshortner.model.UrlModel2
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RestApi {

    @GET("api.php")
    suspend fun getShort(
        @Query("key") key: String,
        @Query("short") url: String
    ): Response<UrlModel>

    @GET("create.php")
    suspend fun getShort2(
        @Query("format") key: String,
        @Query("url") url: String
    ):Response<UrlModel2>
}