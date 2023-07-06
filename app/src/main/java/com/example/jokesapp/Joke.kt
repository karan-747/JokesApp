package com.example.jokesapp

data class Joke(
    val category: String,
    val delivery: String,
    val flags: Flags,
    val id: Int,
    val lang: String,
    val safe: Boolean,
    val setup: String,
    val type: String
)