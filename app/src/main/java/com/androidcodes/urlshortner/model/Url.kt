package com.androidcodes.urlshortner.model

data class Url(
    val date: String,
    val fullLink: String,
    val shortLink: String,
    val status: Int,
    val title: String
)
