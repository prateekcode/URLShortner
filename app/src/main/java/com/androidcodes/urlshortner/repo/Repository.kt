package com.androidcodes.urlshortner.repo

import androidx.lifecycle.LiveData
import com.androidcodes.urlshortner.api.RetrofitInstance
import com.androidcodes.urlshortner.data.UrlDao
import com.androidcodes.urlshortner.data.UrlDatabase
import com.androidcodes.urlshortner.data.model.UrlData
import com.androidcodes.urlshortner.model.UrlModel
import retrofit2.Response

class Repository(private val urlDao: UrlDao) {

    suspend fun getApiResult(apiKey: String, shortUrl: String): Response<UrlModel>{
        return RetrofitInstance.api.getShort(apiKey, shortUrl)
    }

    val getAllData: LiveData<List<UrlData>> = urlDao.getAllData()

    suspend fun insertData(urlData: UrlData){
        urlDao.insertData(urlData)
    }

    suspend fun deleteData(){
        urlDao.deleteAll()
    }
}
