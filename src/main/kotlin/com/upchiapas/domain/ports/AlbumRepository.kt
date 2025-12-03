package com.fernando.com.upchiapas.domain.ports

import com.upchiapas.domain.models.Album
import java.util.UUID

/**
 * Puerto de salida para el repositorio de Album
 * Define el contrato que debe implementar la capa de infraestructura
 */
interface AlbumRepository {
    suspend fun create(album: Album): Album
    suspend fun findById(id: UUID): Album?
    suspend fun findAll(): List<Album>
    suspend fun update(id: UUID, album: Album): Album?
    suspend fun delete(id: UUID): Boolean
    suspend fun findByArtistId(artistId: UUID): List<Album>
    suspend fun findByIdWithRelations(id: UUID): Album?
}