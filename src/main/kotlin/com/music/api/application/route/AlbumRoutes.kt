package com.music.api.application.route

import com.music.api.application.service.AlbumService
import com.music.api.infrastructure.dto.CreateAlbumDTO
import com.music.api.infrastructure.dto.UpdateAlbumDTO
import com.music.api.infrastructure.mapper.AlbumMapper
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID

fun Route.albumRoutes(albumService: AlbumService) {
    
    route("/albumes") {
        
        // CREATE - POST /api/albumes
        post {
            try {
                val dto = call.receive<CreateAlbumDTO>()
                val album = albumService.createAlbum(dto)
                val response = AlbumMapper.toResponseDTO(album)
                call.respond(HttpStatusCode.Created, response)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Invalid request")))
            }
        }
        
        // READ ALL - GET /api/albumes
        get {
            try {
                val albums = albumService.getAllAlbums()
                val response = albums.map { AlbumMapper.toResponseDTO(it) }
                call.respond(HttpStatusCode.OK, response)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Internal error")))
            }
        }
        
        // READ ONE - GET /api/albumes/{id}
        get("/{id}") {
            try {
                val id = UUID.fromString(call.parameters["id"])
                val album = albumService.getAlbumById(id)
                
                if (album != null) {
                    val response = AlbumMapper.toResponseDTO(album)
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Album not found"))
                }
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid UUID format"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Internal error")))
            }
        }
        
        // UPDATE - PUT /api/albumes/{id}
        put("/{id}") {
            try {
                val id = UUID.fromString(call.parameters["id"])
                val dto = call.receive<UpdateAlbumDTO>()
                val album = albumService.updateAlbum(id, dto)
                
                if (album != null) {
                    val response = AlbumMapper.toResponseDTO(album)
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Album not found"))
                }
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid UUID format"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Invalid request")))
            }
        }
        
        // DELETE - DELETE /api/albumes/{id}
        delete("/{id}") {
            try {
                val id = UUID.fromString(call.parameters["id"])
                val deleted = albumService.deleteAlbum(id)
                
                if (deleted) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Album not found"))
                }
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid UUID format"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Internal error")))
            }
        }
    }
}
