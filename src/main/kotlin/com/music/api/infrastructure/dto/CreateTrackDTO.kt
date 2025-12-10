package com.music.api.infrastructure.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateTrackDTO(
    val title: String,
    val duration: Int,
    val albumId: String
)
