package com.androidcodes.urlshortner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.androidcodes.urlshortner.repo.Repository
import com.androidcodes.urlshortner.utils.Constants.Companion.API_KEY
import com.androidcodes.urlshortner.views.ApiViewModel
import com.androidcodes.urlshortner.views.ApiViewModelFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.new_activity.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: ApiViewModel
    private lateinit var shortenBtn: CircularProgressButton
    private lateinit var invalidText: TextView
    private lateinit var editTextLongUrl: EditText
    private lateinit var longUrl: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_activity)

        editTextLongUrl = et_long_url
        val repo = Repository()
        val viewModelFactory = ApiViewModelFactory(repo)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ApiViewModel::class.java)

        shortenBtn = shorten_btn
        shortenBtn.setOnClickListener {
            if (et_long_url.text.isNotEmpty()){
                longUrl = editTextLongUrl.text.toString()
                invalidText = invalid_url
                invalidText.visibility = View.VISIBLE
                layout_no_data.visibility = View.VISIBLE
                shortenBtn.startAnimation()
                viewModel.shortUrl(API_KEY, longUrl)
                viewModel.apiResponse.observe(this, Observer { response ->
                    if (response.isSuccessful) {
                        shortenBtn.revertAnimation()
                        et_long_url.setText("")
                        invalidText.text = response.body()!!.url.shortLink
                        Log.d("RESPONSE", "Getting the response body: ${response.body()}")
                        Log.d("RESPONSE", "Getting the response short link: ${response.body()!!.url.shortLink}")
                    } else {
                        et_long_url.setText("")
                        shortenBtn.revertAnimation()
                        Log.d("RESPONSE", "Getting the response error: ${response.errorBody()}")
                    }
                })

            }else{
                et_long_url.requestFocus()
                Toast.makeText(applicationContext, "Url must be provided", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
        }

    }
}