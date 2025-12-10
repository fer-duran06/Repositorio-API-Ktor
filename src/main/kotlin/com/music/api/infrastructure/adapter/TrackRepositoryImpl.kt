package com.music.api.infrastructure.adapter

import com.music.api.domain.model.Track
import com.music.api.domain.port.TrackRepository
import com.music.api.infrastructure.persistence.table.TracksTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.Instant
import java.util.UUID

class TrackRepositoryImpl : TrackRepository {
    
    private suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction { block() }
    
    override suspend fun create(track: Track): Track = dbQuery {
        val id = TracksTable.insertAndGetId {
            it[this.id] = track.id
            it[title] = track.title
            it[duration] = track.duration
            it[albumId] = track.albumId
            it[createdAt] = track.createdAt
            it[updatedAt] = track.updatedAt
        }
        
        track.copy(id = id.value)
    }
    
    override suspend fun findById(id: UUID): Track? = dbQuery {
        TracksTable.selectAll()
            .where { TracksTable.id eq id }
            .map { row ->
                Track(
                    id = row[TracksTable.id].value,
                    title = row[TracksTable.title],
                    duration = row[TracksTable.duration],
                    albumId = row[TracksTable.albumId].value,
                    createdAt = row[TracksTable.createdAt],
                    updatedAt = row[TracksTable.updatedAt]
                )
            }
            .singleOrNull()
    }
    
    override suspend fun findAll(): List<Track> = dbQuery {
        TracksTable.selectAll().map { row ->
            Track(
                id = row[TracksTable.id].value,
                title = row[TracksTable.title],
                duration = row[TracksTable.duration],
                albumId = row[TracksTable.albumId].value,
                createdAt = row[TracksTable.createdAt],
                updatedAt = row[TracksTable.updatedAt]
            )
        }
    }
    
    override suspend fun findByAlbumId(albumId: UUID): List<Track> = dbQuery {
        TracksTable.selectAll()
            .where { TracksTable.albumId eq albumId }
            .map { row ->
                Track(
                    id = row[TracksTable.id].value,
                    title = row[TracksTable.title],
                    duration = row[TracksTable.duration],
                    albumId = row[TracksTable.albumId].value,
                    createdAt = row[TracksTable.createdAt],
                    updatedAt = row[TracksTable.updatedAt]
                )
            }
    }
    
    override suspend fun update(id: UUID, track: Track): Track? = dbQuery {
        val updated = TracksTable.update({ TracksTable.id eq id }) {
            it[title] = track.title
            it[duration] = track.duration
            it[albumId] = track.albumId
            it[updatedAt] = Instant.now()
        }
        
        if (updated > 0) findById(id) else null
    }
    
    override suspend fun delete(id: UUID): Boolean = dbQuery {
        TracksTable.deleteWhere { TracksTable.id eq id } > 0
    }
}
