package com.androidcodes.urlshortner.utils

import androidx.recyclerview.widget.DiffUtil
import com.androidcodes.urlshortner.data.model.UrlData

class ResponseUtil(private val oldList: List<UrlData>, private val newList: List<UrlData>): DiffUtil.Callback(){

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].longUrl == newList[newItemPosition].longUrl
                && oldList[oldItemPosition].shortUrl == newList[newItemPosition].shortUrl
    }
}