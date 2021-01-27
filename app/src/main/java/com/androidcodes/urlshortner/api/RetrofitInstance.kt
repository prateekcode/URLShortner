package com.androidcodes.urlshortner.api

import com.androidcodes.urlshortner.utils.Constants.Companion.BASE_URL
import com.androidcodes.urlshortner.utils.Constants.Companion.BASE_URL_2
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: RestApi by lazy {
        retrofit.create(RestApi::class.java)
    }
}
object RetrofitInstance2{

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL_2)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: RestApi2 by lazy {
        retrofit.create(RestApi2::class.java)
    }
}