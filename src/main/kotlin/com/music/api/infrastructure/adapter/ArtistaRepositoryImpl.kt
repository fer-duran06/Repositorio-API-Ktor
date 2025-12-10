package com.music.api.infrastructure.adapter

import com.music.api.domain.model.Album
import com.music.api.domain.model.Artista
import com.music.api.domain.port.ArtistaRepository
import com.music.api.infrastructure.persistence.table.AlbumesTable
import com.music.api.infrastructure.persistence.table.ArtistasTable
import com.music.api.infrastructure.persistence.table.TracksTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Instant
import java.util.UUID

class ArtistaRepositoryImpl : ArtistaRepository {
    
    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction { block() }
    
    override suspend fun create(artista: Artista): Artista = dbQuery {
        val id = ArtistasTable.insertAndGetId {
            it[this.id] = artista.id
            it[name] = artista.name
            it[genre] = artista.genre
            it[createdAt] = artista.createdAt
            it[updatedAt] = artista.updatedAt
        }
        
        artista.copy(id = id.value)
    }
    
    override suspend fun findById(id: UUID): Artista? = dbQuery {
        val artistaRow = ArtistasTable.selectAll()
            .where { ArtistasTable.id eq id }
            .singleOrNull() ?: return@dbQuery null
        
        // Buscar albums relacionados
        val albums = AlbumesTable
            .selectAll()
            .where { AlbumesTable.artistId eq id }
            .map { albumRow ->
                // Buscar tracks de cada album
                val tracks = TracksTable
                    .selectAll()
                    .where { TracksTable.albumId eq albumRow[AlbumesTable.id].value }
                    .map { trackRow ->
                        com.music.api.domain.model.Track(
                            id = trackRow[TracksTable.id].value,
                            title = trackRow[TracksTable.title],
                            duration = trackRow[TracksTable.duration],
                            albumId = trackRow[TracksTable.albumId].value,
                            createdAt = trackRow[TracksTable.createdAt],
                            updatedAt = trackRow[TracksTable.updatedAt]
                        )
                    }
                
                Album(
                    id = albumRow[AlbumesTable.id].value,
                    title = albumRow[AlbumesTable.title],
                    releaseYear = albumRow[AlbumesTable.releaseYear],
                    artistId = albumRow[AlbumesTable.artistId].value,
                    createdAt = albumRow[AlbumesTable.createdAt],
                    updatedAt = albumRow[AlbumesTable.updatedAt],
                    tracks = tracks
                )
            }
        
        Artista(
            id = artistaRow[ArtistasTable.id].value,
            name = artistaRow[ArtistasTable.name],
            genre = artistaRow[ArtistasTable.genre],
            createdAt = artistaRow[ArtistasTable.createdAt],
            updatedAt = artistaRow[ArtistasTable.updatedAt],
            albums = albums
        )
    }
    
    override suspend fun findAll(): List<Artista> = dbQuery {
        ArtistasTable.selectAll().map { row ->
            Artista(
                id = row[ArtistasTable.id].value,
                name = row[ArtistasTable.name],
                genre = row[ArtistasTable.genre],
                createdAt = row[ArtistasTable.createdAt],
                updatedAt = row[ArtistasTable.updatedAt]
            )
        }
    }
    
    override suspend fun update(id: UUID, artista: Artista): Artista? = dbQuery {
        val updated = ArtistasTable.update({ ArtistasTable.id eq id }) {
            it[name] = artista.name
            it[genre] = artista.genre
            it[updatedAt] = Instant.now()
        }
        
        if (updated > 0) findById(id) else null
    }
    
    override suspend fun delete(id: UUID): Boolean = dbQuery {
        ArtistasTable.deleteWhere { ArtistasTable.id eq id } > 0
    }
}
