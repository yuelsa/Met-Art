package com.example.metart.repository

data class Artwork(
    val id: Int,
    val title: String,
    val artist: String,
    val imageUrl: String,
    val year: String?,
)
