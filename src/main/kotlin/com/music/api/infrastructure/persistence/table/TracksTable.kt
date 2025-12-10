package com.music.api.infrastructure.persistence.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.timestamp

object TracksTable : UUIDTable("tracks") {
    val title = varchar("title", 150)
    val duration = integer("duration")
    val albumId = reference("album_id", AlbumesTable)
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}
