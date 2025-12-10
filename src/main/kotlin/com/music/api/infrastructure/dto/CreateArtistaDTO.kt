package com.music.api.infrastructure.dto

import kotlinx.serialization.Serializable

@Serializable
data class CreateArtistaDTO(
    val name: String,
    val genre: String? = null
)
