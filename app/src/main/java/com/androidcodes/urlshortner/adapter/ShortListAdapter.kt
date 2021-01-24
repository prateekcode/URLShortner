package com.androidcodes.urlshortner.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androidcodes.urlshortner.R
import com.androidcodes.urlshortner.data.model.UrlData
import com.androidcodes.urlshortner.utils.ResponseUtil
import kotlinx.android.synthetic.main.single_item.view.*

class ShortListAdapter(private val onItemClickListener: OnItemClickListener): RecyclerView.Adapter<ShortListAdapter.MyViewHolder>() {

    var dataList = emptyList<UrlData>()

    inner class MyViewHolder(view: View, onItemClickListener: OnItemClickListener):RecyclerView.ViewHolder(view){
        val shortUrl = view.short_url
        val longUrl = view.long_url
        init {
            view.share_short_url.setOnClickListener {
                onItemClickListener.onShareBtnClick(adapterPosition)
            }
            view.copy_short_url.setOnClickListener {
                onItemClickListener.onCopyBtnClick(adapterPosition)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.single_item, parent, false)
        return MyViewHolder(itemView, onItemClickListener)
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

    interface OnItemClickListener{
        fun onShareBtnClick(position: Int)
        fun onCopyBtnClick(position: Int)
    }

}