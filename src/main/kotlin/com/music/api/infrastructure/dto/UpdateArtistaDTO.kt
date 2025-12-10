package com.music.api.infrastructure.dto

import kotlinx.serialization.Serializable

@Serializable
data class UpdateArtistaDTO(
    val name: String? = null,
    val genre: String? = null
)
