package com.fernando.com.upchiapas.infrastructure.adapters


import com.upchiapas.domain.models.Track
import java.util.UUID

/**
 * Puerto de salida para el repositorio de Track
 * Define el contrato que debe implementar la capa de infraestructura
 */
interface TrackRepository {
    suspend fun create(track: Track): Track
    suspend fun findById(id: UUID): Track?
    suspend fun findAll(): List<Track>
    suspend fun update(id: UUID, track: Track): Track?
    suspend fun delete(id: UUID): Boolean
    suspend fun findByAlbumId(albumId: UUID): List<Track>
}