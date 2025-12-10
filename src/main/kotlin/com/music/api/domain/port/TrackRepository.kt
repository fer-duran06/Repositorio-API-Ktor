package com.music.api.domain.port

import com.music.api.domain.model.Track
import java.util.UUID

interface TrackRepository {
    suspend fun create(track: Track): Track
    suspend fun findById(id: UUID): Track?
    suspend fun findAll(): List<Track>
    suspend fun findByAlbumId(albumId: UUID): List<Track>
    suspend fun update(id: UUID, track: Track): Track?
    suspend fun delete(id: UUID): Boolean
}
