package com.example.metart.network

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MetApi {
    @GET("search")
    suspend fun searchObjects(
        @Query("q") query: String,
        @Query("hasImages") hasImages: Boolean = true,
        @Query("artistOrCulture") artistOrCulture: Boolean = true,
    ): SearchResponse

    @GET("objects/{objectID}")
    suspend fun getObject(@Path("objectID") objectId: Int): MetObject
}

@Serializable
data class SearchResponse(
    @SerialName("total") val total: Int,
    @SerialName("objectIDs") val objectIDs: List<Int>? = null,
)

@Serializable
data class MetObject(
    @SerialName("objectID") val objectID: Int? = null,
    @SerialName("title") val title: String? = null,
    @SerialName("artistDisplayName") val artist: String? = null,
    @SerialName("primaryImageSmall") val imageSmall: String? = null,
    @SerialName("primaryImage") val imageFull: String? = null,
    @SerialName("objectDate") val objectDate: String? = null,
    @SerialName("isPublicDomain") val isPublicDomain: Boolean = false,
)