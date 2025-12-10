package com.music.api.infrastructure.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateAlbumDTO(
    val title: String,
    val releaseYear: Int,
    val artistId: String
)
