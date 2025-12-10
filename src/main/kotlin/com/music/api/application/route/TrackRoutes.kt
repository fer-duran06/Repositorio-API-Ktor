package com.music.api.application.route

import com.music.api.application.service.TrackService
import com.music.api.infrastructure.dto.CreateTrackDTO
import com.music.api.infrastructure.dto.UpdateTrackDTO
import com.music.api.infrastructure.mapper.TrackMapper
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID

fun Route.trackRoutes(trackService: TrackService) {
    
    route("/tracks") {
        
        // CREATE - POST /api/tracks
        post {
            try {
                val dto = call.receive<CreateTrackDTO>()
                val track = trackService.createTrack(dto)
                val response = TrackMapper.toResponseDTO(track)
                call.respond(HttpStatusCode.Created, response)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Invalid request")))
            }
        }
        
        // READ ALL - GET /api/tracks
        get {
            try {
                val tracks = trackService.getAllTracks()
                val response = tracks.map { TrackMapper.toResponseDTO(it) }
                call.respond(HttpStatusCode.OK, response)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Internal error")))
            }
        }
        
        // READ ONE - GET /api/tracks/{id}
        get("/{id}") {
            try {
                val id = UUID.fromString(call.parameters["id"])
                val track = trackService.getTrackById(id)
                
                if (track != null) {
                    val response = TrackMapper.toResponseDTO(track)
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Track not found"))
                }
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid UUID format"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Internal error")))
            }
        }
        
        // UPDATE - PUT /api/tracks/{id}
        put("/{id}") {
            try {
                val id = UUID.fromString(call.parameters["id"])
                val dto = call.receive<UpdateTrackDTO>()
                val track = trackService.updateTrack(id, dto)
                
                if (track != null) {
                    val response = TrackMapper.toResponseDTO(track)
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Track not found"))
                }
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid UUID format"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Invalid request")))
            }
        }
        
        // DELETE - DELETE /api/tracks/{id}
        delete("/{id}") {
            try {
                val id = UUID.fromString(call.parameters["id"])
                val deleted = trackService.deleteTrack(id)
                
                if (deleted) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Track not found"))
                }
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid UUID format"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Internal error")))
            }
        }
    }
}
