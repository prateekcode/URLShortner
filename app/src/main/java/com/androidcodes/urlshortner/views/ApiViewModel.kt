package com.androidcodes.urlshortner.views

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.androidcodes.urlshortner.data.model.UrlData
import com.androidcodes.urlshortner.model.UrlModel
import com.androidcodes.urlshortner.repo.Repository
import com.androidcodes.urlshortner.utils.Resource
import kotlinx.coroutines.Dispatchers
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

    fun insertData(urlData: UrlData){
        viewModelScope.launch{
            repository.insertData(urlData)
        }
    }

    fun deleteData(){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteData()
        }
    }

    fun getSavedData() = repository.getAllData

    private fun handleShortUrl(response: Response<UrlModel>): Resource<UrlModel> {
        if (response.isSuccessful){
            response.body()?.let {
                resultResponse ->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

}
