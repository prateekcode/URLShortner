package com.androidcodes.urlshortner.repo

import com.androidcodes.urlshortner.api.RetrofitInstance
import com.androidcodes.urlshortner.model.UrlModel
import retrofit2.Response

class Repository {
    suspend fun getApiResult(apiKey: String, shortUrl: String): Response<UrlModel>{
        return RetrofitInstance.api.getShort(apiKey, shortUrl)
    }
}
