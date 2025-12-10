package com.music.api.application.route

import com.music.api.application.service.ArtistaService
import com.music.api.infrastructure.dto.CreateArtistaDTO
import com.music.api.infrastructure.dto.UpdateArtistaDTO
import com.music.api.infrastructure.mapper.ArtistaMapper
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.util.UUID

fun Route.artistaRoutes(artistaService: ArtistaService) {
    
    route("/artistas") {
        
        // CREATE - POST /api/artistas
        post {
            try {
                val dto = call.receive<CreateArtistaDTO>()
                val artista = artistaService.createArtista(dto)
                val response = ArtistaMapper.toResponseDTO(artista)
                call.respond(HttpStatusCode.Created, response)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Invalid request")))
            }
        }
        
        // READ ALL - GET /api/artistas
        get {
            try {
                val artistas = artistaService.getAllArtistas()
                val response = artistas.map { ArtistaMapper.toResponseDTO(it) }
                call.respond(HttpStatusCode.OK, response)
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Internal error")))
            }
        }
        
        // READ ONE - GET /api/artistas/{id}
        get("/{id}") {
            try {
                val id = UUID.fromString(call.parameters["id"])
                val artista = artistaService.getArtistaById(id)
                
                if (artista != null) {
                    val response = ArtistaMapper.toResponseDTO(artista)
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Artista not found"))
                }
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid UUID format"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Internal error")))
            }
        }
        
        // UPDATE - PUT /api/artistas/{id}
        put("/{id}") {
            try {
                val id = UUID.fromString(call.parameters["id"])
                val dto = call.receive<UpdateArtistaDTO>()
                val artista = artistaService.updateArtista(id, dto)
                
                if (artista != null) {
                    val response = ArtistaMapper.toResponseDTO(artista)
                    call.respond(HttpStatusCode.OK, response)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Artista not found"))
                }
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid UUID format"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to (e.message ?: "Invalid request")))
            }
        }
        
        // DELETE - DELETE /api/artistas/{id}
        delete("/{id}") {
            try {
                val id = UUID.fromString(call.parameters["id"])
                val deleted = artistaService.deleteArtista(id)
                
                if (deleted) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, mapOf("error" to "Artista not found"))
                }
            } catch (e: IllegalArgumentException) {
                call.respond(HttpStatusCode.BadRequest, mapOf("error" to "Invalid UUID format"))
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError, mapOf("error" to (e.message ?: "Internal error")))
            }
        }
    }
}
