package com.androidcodes.urlshortner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androidcodes.urlshortner.R
import com.androidcodes.urlshortner.data.model.UrlData
import com.androidcodes.urlshortner.utils.ResponseUtil
import kotlinx.android.synthetic.main.single_item.view.*

class ShortListAdapter: RecyclerView.Adapter<ShortListAdapter.MyViewHolder>() {

    var dataList = emptyList<UrlData>()

    inner class MyViewHolder(view: View):RecyclerView.ViewHolder(view){
        val shortUrl = view.short_url
        val longUrl = view.long_url
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.single_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.shortUrl.text = dataList[position].shortUrl
        holder.longUrl.text = dataList[position].longUrl
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(urlData: List<UrlData>){
        val responseDiffUtil = ResponseUtil(dataList,urlData)
        val responseUtilResult = DiffUtil.calculateDiff(responseDiffUtil)
        this.dataList = urlData
        responseUtilResult.dispatchUpdatesTo(this)
    }

}