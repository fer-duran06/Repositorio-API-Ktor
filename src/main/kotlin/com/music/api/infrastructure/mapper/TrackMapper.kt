package com.music.api.infrastructure.mapper

import com.music.api.domain.model.Track
import com.music.api.infrastructure.dto.CreateTrackDTO
import com.music.api.infrastructure.dto.TrackResponseDTO
import java.util.UUID

object TrackMapper {
    
    fun toModel(dto: CreateTrackDTO): Track {
        return Track(
            title = dto.title,
            duration = dto.duration,
            albumId = UUID.fromString(dto.albumId)
        )
    }
    
    fun toResponseDTO(model: Track): TrackResponseDTO {
        return TrackResponseDTO(
            id = model.id.toString(),
            title = model.title,
            duration = model.duration,
            albumId = model.albumId.toString(),
            createdAt = model.createdAt.toString(),
            updatedAt = model.updatedAt.toString()
        )
    }
}
