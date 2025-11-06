package com.example.metart.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.metart.repository.Artwork

import androidx.lifecycle.viewModelScope


import com.example.metart.network.NetworkModule
import com.example.metart.repository.MetRepository
import com.example.metart.ui.theme.UiState

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class GalleryViewModel : ViewModel() {

    private val repo = MetRepository(NetworkModule.api)

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun updateQuery(q: String) {
        _state.value = _state.value.copy(query = q)
    }

    fun toggleSort() {
        _state.value = _state.value.copy(sortAsc = !_state.value.sortAsc)
        applySort()
    }

    fun search() {
        val q = _state.value.query.trim()
        if (q.isEmpty()) return
        _state.value = _state.value.copy(loading = true, error = null)
        viewModelScope.launch {
            runCatching {
                repo.searchArtworks(query = q, limit = 20, onlyPublicDomain = true)
            }.onSuccess { list ->
                _state.value = _state.value.copy(loading = false, items = sort(list))
            }.onFailure { e ->
                val msg = when (e) {
                    is java.net.UnknownHostException -> "Please check your internet connection."
                    is retrofit2.HttpException -> "Server error (${e.code()}) — please try again later."
                    is kotlinx.serialization.SerializationException -> "A data parsing error occurred."
                    is java.net.SocketTimeoutException -> "Server response is taking too long. Please try again."
                    else -> e.message ?: "An unknown error occurred."
                }
                _state.value = _state.value.copy(loading = false, error = msg)
                Log.e("MetSearch", "search failed", e)
            }
        }
    }

    private fun applySort() {
        _state.value = _state.value.copy(items = sort(_state.value.items))
    }

    private fun sort(list: List<Artwork>): List<Artwork> {
        val cmp = compareBy<Artwork> { it.artist.lowercase() }
        return if (_state.value.sortAsc) list.sortedWith(cmp) else list.sortedWith(cmp.reversed())
    }
}