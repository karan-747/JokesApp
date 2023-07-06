package com.example.jokesapp

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {


    companion object{
        private val BASE_URL = "https://v2.jokeapi.dev/"
        fun getRetrofitInstance():Retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create())).build()

    }
}