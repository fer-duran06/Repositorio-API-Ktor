package com.music.api.application.service

import com.music.api.domain.model.Artista
import com.music.api.domain.port.ArtistaRepository
import com.music.api.infrastructure.dto.CreateArtistaDTO
import com.music.api.infrastructure.dto.UpdateArtistaDTO
import com.music.api.infrastructure.mapper.ArtistaMapper
import java.util.UUID

class ArtistaService(private val artistaRepository: ArtistaRepository) {
    
    suspend fun createArtista(dto: CreateArtistaDTO): Artista {
        val artista = ArtistaMapper.toModel(dto)
        return artistaRepository.create(artista)
    }
    
    suspend fun getArtistaById(id: UUID): Artista? {
        return artistaRepository.findById(id)
    }
    
    suspend fun getAllArtistas(): List<Artista> {
        return artistaRepository.findAll()
    }
    
    suspend fun updateArtista(id: UUID, dto: UpdateArtistaDTO): Artista? {
        val existing = artistaRepository.findById(id) ?: return null
        
        val updated = existing.copy(
            name = dto.name ?: existing.name,
            genre = dto.genre ?: existing.genre
        )
        
        return artistaRepository.update(id, updated)
    }
    
    suspend fun deleteArtista(id: UUID): Boolean {
        return artistaRepository.delete(id)
    }
}
