package com.music.api.infrastructure.persistence.table

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.timestamp

object ArtistasTable : UUIDTable("artistas") {
    val name = varchar("name", 100)
    val genre = varchar("genre", 50).nullable()
    val createdAt = timestamp("created_at")
    val updatedAt = timestamp("updated_at")
}
