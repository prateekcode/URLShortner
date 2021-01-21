package com.androidcodes.urlshortner.views

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.androidcodes.urlshortner.repo.Repository

class ApiViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ApiViewModel(repository) as T
    }
}