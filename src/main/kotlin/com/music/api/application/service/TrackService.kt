package com.music.api.application.service

import com.music.api.domain.model.Track
import com.music.api.domain.port.TrackRepository
import com.music.api.infrastructure.dto.CreateTrackDTO
import com.music.api.infrastructure.dto.UpdateTrackDTO
import com.music.api.infrastructure.mapper.TrackMapper
import java.util.UUID

class TrackService(private val trackRepository: TrackRepository) {
    
    suspend fun createTrack(dto: CreateTrackDTO): Track {
        val track = TrackMapper.toModel(dto)
        return trackRepository.create(track)
    }
    
    suspend fun getTrackById(id: UUID): Track? {
        return trackRepository.findById(id)
    }
    
    suspend fun getAllTracks(): List<Track> {
        return trackRepository.findAll()
    }
    
    suspend fun getTracksByAlbumId(albumId: UUID): List<Track> {
        return trackRepository.findByAlbumId(albumId)
    }
    
    suspend fun updateTrack(id: UUID, dto: UpdateTrackDTO): Track? {
        val existing = trackRepository.findById(id) ?: return null
        
        val updated = existing.copy(
            title = dto.title ?: existing.title,
            duration = dto.duration ?: existing.duration,
            albumId = dto.albumId?.let { UUID.fromString(it) } ?: existing.albumId
        )
        
        return trackRepository.update(id, updated)
    }
    
    suspend fun deleteTrack(id: UUID): Boolean {
        return trackRepository.delete(id)
    }
}
