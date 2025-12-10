package com.music.api.domain.port

import com.music.api.domain.model.Album
import java.util.UUID

interface AlbumRepository {
    suspend fun create(album: Album): Album
    suspend fun findById(id: UUID): Album?
    suspend fun findAll(): List<Album>
    suspend fun findByArtistId(artistId: UUID): List<Album>
    suspend fun update(id: UUID, album: Album): Album?
    suspend fun delete(id: UUID): Boolean
}
