package com.androidcodes.urlshortner.data.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "url_table")

data class UrlData(
    @PrimaryKey @ColumnInfo(name = "Long Url")
    var longUrl: String,
    @ColumnInfo(name = "Short Url")
    var shortUrl: String,
    var urlTitle:String
)
