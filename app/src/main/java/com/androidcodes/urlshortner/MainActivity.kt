package com.androidcodes.urlshortner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.androidcodes.urlshortner.repo.Repository
import com.androidcodes.urlshortner.utils.Constants.Companion.API_KEY
import com.androidcodes.urlshortner.views.ApiViewModel
import com.androidcodes.urlshortner.views.ApiViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ApiViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val repo = Repository()
        val viewModelFactory = ApiViewModelFactory(repo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ApiViewModel::class.java)
        viewModel.shortUrl(API_KEY, "https://dribbble.com/shots/following/mobile")
        viewModel.apiResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {
                text_view_url.text = response.body()!!.url.shortLink
                Log.d("RESPONSE", "Getting the response body: ${response.body()}")
                Log.d("RESPONSE", "Getting the response short link: ${response.body()!!.url.shortLink}")
            } else {
                Log.d("RESPONSE", "Getting the response error: ${response.errorBody()}")
            }
        })
    }
}