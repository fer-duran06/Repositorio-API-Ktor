package com.music.api.infrastructure.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateAlbumDTO(
    val title: String? = null,
    val releaseYear: Int? = null,
    val artistId: String? = null
)
