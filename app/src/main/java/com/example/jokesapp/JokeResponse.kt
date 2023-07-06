package com.example.jokesapp

data class JokeResponse(
    val amount: Int,
    val error: Boolean,
    val jokes: List<Joke>
)