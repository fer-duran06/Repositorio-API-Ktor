package com.music.api.infrastructure.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateTrackDTO(
    val title: String? = null,
    val duration: Int? = null,
    val albumId: String? = null
)
