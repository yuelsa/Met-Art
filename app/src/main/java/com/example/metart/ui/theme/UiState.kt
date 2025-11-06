package com.example.metart.ui.theme

import com.example.metart.repository.Artwork

data class UiState(
    val loading: Boolean = false,
    val query: String = "",
    val sortAsc: Boolean = true,
    val items: List<Artwork> = emptyList(),
    val error: String? = null
)