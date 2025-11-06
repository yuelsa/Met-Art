package com.example.metart.repository


import com.example.metart.network.MetApi

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import kotlin.math.min

class MetRepository(private val api: MetApi) {

    suspend fun searchArtworks(
        query: String,
        limit: Int = 20,
        onlyPublicDomain: Boolean = true,
    ): List<Artwork> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()


        val r1 = api.searchObjects(query = query, hasImages = true, artistOrCulture = true)
        var ids = r1.objectIDs.orEmpty()

        var cameFromFallback = false

        if (ids.isEmpty()) {
            val r2 = api.searchObjects(query = query, hasImages = true, artistOrCulture = false)
            ids = r2.objectIDs.orEmpty()
            cameFromFallback = true
        }
        if (ids.isEmpty()) return@withContext emptyList()


        val results = mutableListOf<Artwork>()
        val chunkSize = 8
        var index = 0

        while (index < ids.size && results.size < limit) {
            val end = min(index + chunkSize, ids.size)
            val chunk = ids.subList(index, end)

            val details = chunk.map { id ->
                async { runCatching { api.getObject(id) }.getOrNull() }
            }.awaitAll()

            details.filterNotNull().forEach { obj ->

                val image = obj.imageSmall ?: obj.imageFull
                if (image == null) return@forEach


                if (cameFromFallback) {
                    val artist = (obj.artist ?: "").lowercase()
                    val qLower = query.lowercase()

                    val looksLikePerson = query.contains(' ')
                    if (looksLikePerson && artist.isNotEmpty() && !artist.contains(qLower)) {
                        return@forEach
                    }
                }

                if (!onlyPublicDomain || obj.isPublicDomain) {
                    results += Artwork(
                        id = obj.objectID ?: -1,
                        title = (obj.title ?: "Untitled").ifBlank { "Untitled" },
                        artist = (obj.artist ?: "Unknown Artist").ifBlank { "Unknown Artist" },
                        imageUrl = image,
                        year = obj.objectDate
                    )
                }
            }

            index = end
        }

        results
    }
}