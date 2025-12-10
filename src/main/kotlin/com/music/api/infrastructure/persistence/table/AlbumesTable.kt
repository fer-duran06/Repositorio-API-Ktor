package com.music.api.infrastructure.persistence.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.timestamp

object AlbumesTable : UUIDTable("albumes") {
    val title = varchar("title", 150)
    val releaseYear = integer("release_year")
    val artistId = reference("artist_id", ArtistasTable)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}
