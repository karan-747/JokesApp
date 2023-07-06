package com.example.jokesapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

class MainActivityViewModel:ViewModel() {
    private  val api = RetrofitInstance.getRetrofitInstance().create(JokesService::class.java)

       suspend fun getJokeResponse(): Response<JokeResponse> {
           return api.getJokeResponse(10,"twopart")
      }


}