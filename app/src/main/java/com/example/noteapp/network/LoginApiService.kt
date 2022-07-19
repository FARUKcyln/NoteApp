package com.example.noteapp.network

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.http.POST

private const val BASE_URL = "https://jsonplaceholder.typicode.com"


interface LoginApiService {
    @GET("posts")
    fun getUsers(): Call<List<User>>
}
