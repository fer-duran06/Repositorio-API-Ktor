package com.music.api.application.service

import com.music.api.domain.model.Album
import com.music.api.domain.port.AlbumRepository
import com.music.api.infrastructure.dto.CreateAlbumDTO
import com.music.api.infrastructure.dto.UpdateAlbumDTO
import com.music.api.infrastructure.mapper.AlbumMapper
import java.util.UUID

class AlbumService(private val albumRepository: AlbumRepository) {
    
    suspend fun createAlbum(dto: CreateAlbumDTO): Album {
        val album = AlbumMapper.toModel(dto)
        return albumRepository.create(album)
    }
    
    suspend fun getAlbumById(id: UUID): Album? {
        return albumRepository.findById(id)
    }
    
    suspend fun getAllAlbums(): List<Album> {
        return albumRepository.findAll()
    }
    
    suspend fun getAlbumsByArtistId(artistId: UUID): List<Album> {
        return albumRepository.findByArtistId(artistId)
    }
    
    suspend fun updateAlbum(id: UUID, dto: UpdateAlbumDTO): Album? {
        val existing = albumRepository.findById(id) ?: return null
        
        val updated = existing.copy(
            title = dto.title ?: existing.title,
            releaseYear = dto.releaseYear ?: existing.releaseYear,
            artistId = dto.artistId?.let { UUID.fromString(it) } ?: existing.artistId
        )
        
        return albumRepository.update(id, updated)
    }
    
    suspend fun deleteAlbum(id: UUID): Boolean {
        return albumRepository.delete(id)
    }
}
