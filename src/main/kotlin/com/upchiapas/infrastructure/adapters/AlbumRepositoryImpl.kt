package com.fernando.com.upchiapas.infrastructure.adapters

import com.upchiapas.domain.models.Album
import com.upchiapas.domain.ports.AlbumRepository
import com.upchiapas.infrastructure.config.DatabaseFactory.dbQuery
import com.upchiapas.infrastructure.entities.AlbumTable
import com.upchiapas.infrastructure.entities.TrackTable
import com.upchiapas.infrastructure.mappers.AlbumMapper
import com.upchiapas.infrastructure.mappers.TrackMapper
import org.jetbrains.exposed.sql.*
import java.time.Instant
import java.util.UUID

/**
 * Adaptador de infraestructura para el repositorio de Album
 * Implementa las operaciones de persistencia usando Exposed ORM
 */
class AlbumRepositoryImpl : AlbumRepository {

    override suspend fun create(album: Album): Album = dbQuery {
        val now = Instant.now()
        val insertStatement = AlbumTable.insert {
            it[title] = album.title
            it[releaseYear] = album.releaseYear
            it[artistId] = album.artistId
            it[createdAt] = now
            it[updatedAt] = now
        }
        val resultRow = insertStatement.resultedValues?.first()
            ?: throw Exception("No se pudo crear el álbum")
        AlbumMapper.toDomain(resultRow)
    }

    override suspend fun findById(id: UUID): Album? = dbQuery {
        AlbumTable.select { AlbumTable.id eq id }
            .map { AlbumMapper.toDomain(it) }
            .singleOrNull()
    }

    override suspend fun findAll(): List<Album> = dbQuery {
        AlbumTable.selectAll()
            .map { AlbumMapper.toDomain(it) }
    }

    override suspend fun update(id: UUID, album: Album): Album? = dbQuery {
        val updated = AlbumTable.update({ AlbumTable.id eq id }) {
            it[title] = album.title
            it[releaseYear] = album.releaseYear
            it[artistId] = album.artistId
            it[updatedAt] = Instant.now()
        }

        if (updated > 0) findById(id) else null
    }

    override suspend fun delete(id: UUID): Boolean = dbQuery {
        AlbumTable.deleteWhere { AlbumTable.id eq id } > 0
    }

    override suspend fun findByArtistId(artistId: UUID): List<Album> = dbQuery {
        AlbumTable.select { AlbumTable.artistId eq artistId }
            .map { AlbumMapper.toDomain(it) }
    }

    override suspend fun findByIdWithRelations(id: UUID): Album? = dbQuery {
        val albumRow = AlbumTable.select { AlbumTable.id eq id }
            .singleOrNull() ?: return@dbQuery null

        val album = AlbumMapper.toDomain(albumRow)

        // Obtener los tracks del álbum
        val tracks = TrackTable.select { TrackTable.albumId eq id }
            .map { TrackMapper.toDomain(it) }

        album.copy(tracks = tracks)
    }
}