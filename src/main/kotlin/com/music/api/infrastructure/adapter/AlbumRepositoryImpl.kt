package com.music.api.infrastructure.adapter

import com.music.api.domain.model.Album
import com.music.api.domain.model.Track
import com.music.api.domain.port.AlbumRepository
import com.music.api.infrastructure.persistence.table.AlbumesTable
import com.music.api.infrastructure.persistence.table.TracksTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Instant
import java.util.UUID

class AlbumRepositoryImpl : AlbumRepository {
    
    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction { block() }
    
    override suspend fun create(album: Album): Album = dbQuery {
        val id = AlbumesTable.insertAndGetId {
            it[this.id] = album.id
            it[title] = album.title
            it[releaseYear] = album.releaseYear
            it[artistId] = album.artistId
            it[createdAt] = album.createdAt
            it[updatedAt] = album.updatedAt
        }
        
        album.copy(id = id.value)
    }
    
    override suspend fun findById(id: UUID): Album? = dbQuery {
        val albumRow = AlbumesTable.selectAll()
            .where { AlbumesTable.id eq id }
            .singleOrNull() ?: return@dbQuery null
        
        // Buscar tracks relacionados
        val tracks = TracksTable
            .selectAll()
            .where { TracksTable.albumId eq id }
            .map { trackRow ->
                Track(
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
    
    override suspend fun findAll(): List<Album> = dbQuery {
        AlbumesTable.selectAll().map { row ->
            Album(
                id = row[AlbumesTable.id].value,
                title = row[AlbumesTable.title],
                releaseYear = row[AlbumesTable.releaseYear],
                artistId = row[AlbumesTable.artistId].value,
                createdAt = row[AlbumesTable.createdAt],
                updatedAt = row[AlbumesTable.updatedAt]
            )
        }
    }
    
    override suspend fun findByArtistId(artistId: UUID): List<Album> = dbQuery {
        AlbumesTable.selectAll()
            .where { AlbumesTable.artistId eq artistId }
            .map { row ->
                Album(
                    id = row[AlbumesTable.id].value,
                    title = row[AlbumesTable.title],
                    releaseYear = row[AlbumesTable.releaseYear],
                    artistId = row[AlbumesTable.artistId].value,
                    createdAt = row[AlbumesTable.createdAt],
                    updatedAt = row[AlbumesTable.updatedAt]
                )
            }
    }
    
    override suspend fun update(id: UUID, album: Album): Album? = dbQuery {
        val updated = AlbumesTable.update({ AlbumesTable.id eq id }) {
            it[title] = album.title
            it[releaseYear] = album.releaseYear
            it[artistId] = album.artistId
            it[updatedAt] = Instant.now()
        }
        
        if (updated > 0) findById(id) else null
    }
    
    override suspend fun delete(id: UUID): Boolean = dbQuery {
        AlbumesTable.deleteWhere { AlbumesTable.id eq id } > 0
    }
}
