package com.example.noteapp.network

import com.squareup.moshi.Json

data class User(
    val id: Int,
    // used to map img_src from the JSON to imgSrcUrl in our class
    @Json(name = "title") val title: String
)