package com.music.api.infrastructure.mapper

import com.music.api.domain.model.Artista
import com.music.api.infrastructure.dto.ArtistaResponseDTO
import com.music.api.infrastructure.dto.CreateArtistaDTO
import java.time.Instant
import java.util.UUID

object ArtistaMapper {
    
    fun toModel(dto: CreateArtistaDTO): Artista {
        return Artista(
            name = dto.name,
            genre = dto.genre
        )
    }
    
    fun toResponseDTO(model: Artista): ArtistaResponseDTO {
        return ArtistaResponseDTO(
            id = model.id.toString(),
            name = model.name,
            genre = model.genre,
            createdAt = model.createdAt.toString(),
            updatedAt = model.updatedAt.toString(),
            albums = model.albums?.map { AlbumMapper.toResponseDTO(it) }
        )
    }
}
