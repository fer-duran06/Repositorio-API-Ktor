package com.music.api.infrastructure.dto

import kotlinx.serialization.Serializable

@Serializable
data class TrackResponseDTO(
    val id: String,
    val title: String,
    val duration: Int,
    val albumId: String,
    val createdAt: String,
    val updatedAt: String
)
