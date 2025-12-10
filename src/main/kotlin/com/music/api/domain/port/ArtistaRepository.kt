package com.music.api.domain.port

import com.music.api.domain.model.Artista
import java.util.UUID

interface ArtistaRepository {
    suspend fun create(artista: Artista): Artista
    suspend fun findById(id: UUID): Artista?
    suspend fun findAll(): List<Artista>
    suspend fun update(id: UUID, artista: Artista): Artista?
    suspend fun delete(id: UUID): Boolean
}
