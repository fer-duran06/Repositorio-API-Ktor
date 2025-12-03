package com.fernando.com.upchiapas.application.routes


import com.upchiapas.application.services.TrackService
import com.upchiapas.infrastructure.dto.CreateTrackDTO
import com.upchiapas.infrastructure.mappers.TrackMapper
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID

/**
 * Configuración de rutas para Track
 * Define todos los endpoints REST para operaciones CRUD
 */
fun Route.trackRoutes(service: TrackService) {

    route("/tracks") {

        // POST /api/tracks - Crear nuevo track
        post {
            try {
                val dto = call.receive<CreateTrackDTO>()
                val track = TrackMapper.fromCreateDTO(dto)
                val created = service.createTrack(track)
                val response = TrackMapper.toResponseDTO(created)
                call.respond(HttpStatusCode.Created, response)
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al crear track"))
            }
        }

        // GET /api/tracks - Obtener todos los tracks
        get {
            try {
                val tracks = service.getAllTracks()
                val response = tracks.map { TrackMapper.toResponseDTO(it) }
                call.respond(HttpStatusCode.OK, response)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener tracks"))
            }
        }

        // GET /api/tracks/{id} - Obtener track por ID
        get("/{id}") {
            try {
                val id = UUID.fromString(call.parameters["id"])
                val track = service.getTrackById(id)

                if (track != null) {
                    val response = TrackMapper.toResponseDTO(track)
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Track no encontrado"))
                }
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID inválido"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al obtener track"))
            }
        }

        // PUT /api/tracks/{id} - Actualizar track
        put("/{id}") {
            try {
                val id = UUID.fromString(call.parameters["id"])
                val dto = call.receive<CreateTrackDTO>()
                val track = TrackMapper.fromCreateDTO(dto)
                val updated = service.updateTrack(id, track)

                if (updated != null) {
                    val response = TrackMapper.toResponseDTO(updated)
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Track no encontrado"))
                }
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to e.message))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al actualizar track"))
            }
        }

        // DELETE /api/tracks/{id} - Eliminar track
        delete("/{id}") {
            try {
                val id = UUID.fromString(call.parameters["id"])
                val deleted = service.deleteTrack(id)

                if (deleted) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Track no encontrado"))
                }
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "ID inválido"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to "Error al eliminar track"))
            }
        }
    }
}