package com.androidcodes.urlshortner

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import br.com.simplepass.loadingbutton.customViews.CircularProgressButton
import com.androidcodes.urlshortner.adapter.ShortListAdapter
import com.androidcodes.urlshortner.data.UrlDatabase
import com.androidcodes.urlshortner.data.model.UrlData
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
    private lateinit var shortUrl: String
    private lateinit var recyclerView: RecyclerView
    private val recyclerAdapter: ShortListAdapter by lazy { ShortListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_activity)

        val urlDao = UrlDatabase.getDatabase(application).urlDao()

        editTextLongUrl = et_long_url

        //val viewModelFactory = ApiViewModelFactory(repo)
        //viewModel = ViewModelProvider(this, viewModelFactory).get(ApiViewModel::class.java)
        //urlViewModel = ViewModelProvider(this).get(UrlViewModel::class.java)

       // viewModel = (application as MainActivity).viewModel

        val repository = Repository(urlDao)
        val viewModelFactory = ApiViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ApiViewModel::class.java)

        layout_no_data.visibility = View.GONE
        viewModel.getSavedData()

        // Setup RecyclerView
        setRecyclerView()

        viewModel.getSavedData().observe(this, {
            data -> recyclerAdapter.setData(data)
        })

        shortenBtn = shorten_btn
        shortenBtn.setOnClickListener {
            if (et_long_url.text.isNotEmpty()) {
                longUrl = editTextLongUrl.text.toString()
                shortUrl = ""
                invalidText = invalid_url
                invalidText.visibility = View.VISIBLE
                layout_no_data.visibility = View.GONE
                shortenBtn.startAnimation()
                viewModel.shortUrl(API_KEY, longUrl)
                viewModel.apiResponse.observe(this, Observer { response ->
                    if (response.isSuccessful) {
                        shortenBtn.revertAnimation()
                        et_long_url.setText("")
                        invalidText.text = response.body()!!.url.shortLink
                        shortUrl = response.body()!!.url.shortLink
                        Log.d("RESPONSE", "Getting the response body: ${response.body()}")
                        Log.d(
                            "RESPONSE",
                            "Getting the response short link: ${response.body()!!.url.shortLink}"
                        )
                        val entry = UrlData(longUrl, response.body()!!.url.shortLink)
                        viewModel.insertData(entry)

                    } else {
                        et_long_url.setText("")
                        shortenBtn.revertAnimation()
                        Log.d("RESPONSE", "Getting the response error: ${response.errorBody()}")
                    }
                })

            } else {
                et_long_url.requestFocus()
                Toast.makeText(applicationContext, "Url must be provided", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }
        }

        delete_btn.setOnClickListener {
            viewModel.deleteData()
        }

    }

    //Setting up the RecyclerView
    private fun setRecyclerView(){
        recyclerView = recycler_item
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerAdapter
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
    }







}