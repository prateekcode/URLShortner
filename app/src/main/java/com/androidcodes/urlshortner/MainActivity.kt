package com.androidcodes.urlshortner

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
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
import org.angmarch.views.NiceSpinner
import java.net.URL
import java.util.*


class MainActivity : AppCompatActivity(), ShortListAdapter.OnItemClickListener {

    private lateinit var viewModel: ApiViewModel
    private lateinit var shortenBtn: CircularProgressButton
    private lateinit var invalidText: TextView
    private lateinit var editTextLongUrl: EditText
    private lateinit var longUrl: String
    private lateinit var shortUrl: String
    private lateinit var recyclerView: RecyclerView
    private val recyclerAdapter: ShortListAdapter by lazy { ShortListAdapter(this) }
    private lateinit var dataList: LiveData<List<UrlData>>

    private lateinit var niceSpinner: NiceSpinner
    private lateinit var selectedUrl:String
    private var titleUrl:String=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_activity)


        val urlDao = UrlDatabase.getDatabase(application).urlDao()

        editTextLongUrl = et_long_url

        // Spinner
        niceSpinner = nice_spinner
        val dataset: List<String> = LinkedList(listOf("Is.gd", "Cut.ly"))
        niceSpinner.attachDataSource(dataset)
        selectedUrl=dataset[0]

        niceSpinner.setOnSpinnerItemSelectedListener { parent, view, position, id ->
            val item:String= parent.getItemAtPosition(position) as String
            selectedUrl=item
            Log.d("ITEM", "onCreate:$item ")

        }


        val repository = Repository(urlDao)
        val viewModelFactory = ApiViewModelFactory(repository)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ApiViewModel::class.java)

        layout_no_data.visibility = View.GONE
        viewModel.getSavedData()

        dataList = repository.getAllData

        // Setup RecyclerView
        setRecyclerView()

        viewModel.getSavedData().observe(this, { data ->
            recyclerAdapter.setData(data)
        })

        //calling btn:
        searchBtn()

        delete_btn.setOnClickListener {
            viewModel.deleteData()
        }



    }

    //Search Button :
    private fun searchBtn(){
        shortenBtn = shorten_btn
        shortenBtn.setOnClickListener {
            if (internet_connection()) {
                if (et_long_url.text.isNotEmpty()) {
                    longUrl = editTextLongUrl.text.toString()
                    shortUrl = ""
                    invalidText = invalid_url
                    invalidText.visibility = View.VISIBLE
                    layout_no_data.visibility = View.GONE
                    shortenBtn.startAnimation()

                    val hostUrl = URL(longUrl)
                    var title: String = hostUrl.getHost()

                    title = if (title.startsWith("www.")) title.substring(4) else title
                    for(c in title){
                        if(c=='.')
                            break
                        titleUrl+=c
                    }
                    titleUrl=titleUrl.toUpperCase(Locale.ROOT)
                    Log.d("Title", "onCreate: $titleUrl ")
                    if (selectedUrl=="Cut.ly"){
                        cut_ly(longUrl)
                    }else{
                        Is_gd(longUrl)
                    }
                } else {
                    et_long_url.requestFocus()
                    Toast.makeText(applicationContext, "Url must be provided", Toast.LENGTH_SHORT)
                        .show()
                    return@setOnClickListener
                }
            } else {
                Toast.makeText(applicationContext, "No Internet Connection...", Toast.LENGTH_SHORT)
                    .show()
                return@setOnClickListener
            }

        }
    }

    //APi'S :
    private fun cut_ly(longUrl: String) {
        viewModel.shortUrl(API_KEY, longUrl)
        viewModel.apiResponse.observe(this, Observer { response ->
            if (response.isSuccessful) {
                shortenBtn.revertAnimation()
                et_long_url.setText("")
                invalidText.text = response.body()!!.url.shortLink
                shortUrl = response.body()!!.url.shortLink

                // Adding Data
                val entry = UrlData(longUrl, response.body()!!.url.shortLink,titleUrl)
                viewModel.insertData(entry)
                titleUrl=""

            } else {
                et_long_url.setText("")
                shortenBtn.revertAnimation()
                Log.d("RESPONSE", "Getting the response error: ${response.errorBody()}")
            }
        })
    }

    private fun Is_gd(longUrl: String) {

        viewModel.shortUrl2("json", longUrl)
        viewModel.apiResponse2.observe(this, Observer { response ->
            if (response.isSuccessful) {

                shortenBtn.revertAnimation()
                et_long_url.setText("")
                invalidText.text = response.body()!!.shorturl

                // Adding Data
                val entry = UrlData(longUrl, response.body()!!.shorturl,titleUrl)
                viewModel.insertData(entry)
                titleUrl=""

            } else {
                et_long_url.setText("")
                shortenBtn.revertAnimation()
                Log.d("RESPONSE", "Getting the response error: ${response.errorBody()}")
            }
        })
    }

    //Checking Internet :
    fun internet_connection(): Boolean {
        //Check if connected to internet, output accordingly
        val cm = this.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting
    }

    //Setting up the RecyclerView :
    private fun setRecyclerView() {
        recyclerView = recycler_item
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = recyclerAdapter
        layoutManager.stackFromEnd = true
        layoutManager.reverseLayout = true
    }

    override fun onShareBtnClick(position: Int) {
        val shortUrl = dataList.value!![position].shortUrl
        Log.d("ITEM", "onShareBtnClick: $shortUrl")
        //Toast.makeText(this, "Clicked on $shortUrl", Toast.LENGTH_SHORT).show()
        val shareIntent = Intent(Intent.ACTION_SEND)
        shareIntent.type = "text/plain"
        shareIntent.putExtra(Intent.EXTRA_TEXT, shortUrl)
        //shareIntent.putExtra(Intent.EXTRA_TEXT, "\nDownload our App from the *Google Play Store* play.google.com/urlshortner")
        startActivity(Intent.createChooser(shareIntent, "Share Short URL"))
    }

    override fun onCopyBtnClick(position: Int) {
        val shortUrl = dataList.value!![position].shortUrl
        Toast.makeText(this, "$shortUrl copied!", Toast.LENGTH_SHORT).show()
        val clipBoardManager =
            application.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Copied", shortUrl)
        clipBoardManager.setPrimaryClip(clipData)
    }

}