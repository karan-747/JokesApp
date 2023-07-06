package com.example.jokesapp

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface JokesService {
    @GET("/joke/Any")
    suspend fun  getJokeResponse (@Query("amount") amount:Int, @Query("type") type:String ): Response<JokeResponse>
}