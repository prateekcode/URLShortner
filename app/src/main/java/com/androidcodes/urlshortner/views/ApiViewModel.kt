package com.androidcodes.urlshortner.views

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidcodes.urlshortner.model.UrlModel
import com.androidcodes.urlshortner.repo.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class ApiViewModel(private val repository: Repository): ViewModel() {

    var apiResponse: MutableLiveData<Response<UrlModel>> = MutableLiveData()

    fun shortUrl(apiKey: String, shortUrl: String){
        viewModelScope.launch {
            val response = repository.getApiResult(apiKey, shortUrl)
            apiResponse.value = response
        }
    }

}