package com.music.api.infrastructure.dto

import kotlinx.serialization.Serializable

@Serializable
data class ArtistaResponseDTO(
    val id: String,
    val name: String,
    val genre: String?,
    val createdAt: String,
    val updatedAt: String,
    val albums: List<AlbumResponseDTO>? = null
)
