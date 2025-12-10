package com.music.api

import com.music.api.application.route.albumRoutes
import com.music.api.application.route.artistaRoutes
import com.music.api.application.route.trackRoutes
import com.music.api.application.service.AlbumService
import com.music.api.application.service.ArtistaService
import com.music.api.application.service.TrackService
import com.music.api.infrastructure.adapter.AlbumRepositoryImpl
import com.music.api.infrastructure.adapter.ArtistaRepositoryImpl
import com.music.api.infrastructure.adapter.TrackRepositoryImpl
import com.music.api.infrastructure.config.DatabaseConfig
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

fun main() {
    embeddedServer(Netty, port = 3000, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    // Inicializar base de datos
    DatabaseConfig.init(environment.config)
    
    // Configurar serialización JSON
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    
    // Configurar manejo de errores
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to (cause.message ?: "Unknown error"))
            )
        }
    }
    
    // Inicializar repositorios
    val artistaRepository = ArtistaRepositoryImpl()
    val albumRepository = AlbumRepositoryImpl()
    val trackRepository = TrackRepositoryImpl()
    
    // Inicializar servicios
    val artistaService = ArtistaService(artistaRepository)
    val albumService = AlbumService(albumRepository)
    val trackService = TrackService(trackRepository)
    
    // Configurar rutas
    routing {
        route("/api") {
            artistaRoutes(artistaService)
            albumRoutes(albumService)
            trackRoutes(trackService)
            
            // Health check
            get("/health") {
                call.respond(HttpStatusCode.OK, mapOf("status" to "OK"))
            }
        }
    }
}
