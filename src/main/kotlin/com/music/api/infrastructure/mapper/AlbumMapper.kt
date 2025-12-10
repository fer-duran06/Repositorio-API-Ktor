package com.music.api.infrastructure.mapper

import com.music.api.domain.model.Album
import com.music.api.infrastructure.dto.AlbumResponseDTO
import com.music.api.infrastructure.dto.CreateAlbumDTO
import java.util.UUID

object AlbumMapper {
    
    fun toModel(dto: CreateAlbumDTO): Album {
        return Album(
            title = dto.title,
            releaseYear = dto.releaseYear,
            artistId = UUID.fromString(dto.artistId)
        )
    }
    
    fun toResponseDTO(model: Album): AlbumResponseDTO {
        return AlbumResponseDTO(
            id = model.id.toString(),
            title = model.title,
            releaseYear = model.releaseYear,
            artistId = model.artistId.toString(),
            createdAt = model.createdAt.toString(),
            updatedAt = model.updatedAt.toString(),
            tracks = model.tracks?.map { TrackMapper.toResponseDTO(it) }
        )
    }
}
