package com.fernando.com.upchiapas.domain

import com.upchiapas.application.routes.albumRoutes
import com.upchiapas.application.routes.artistaRoutes
import com.upchiapas.application.routes.trackRoutes
import com.upchiapas.application.services.AlbumService
import com.upchiapas.application.services.ArtistaService
import com.upchiapas.application.services.TrackService
import com.upchiapas.infrastructure.adapters.AlbumRepositoryImpl
import com.upchiapas.infrastructure.adapters.ArtistaRepositoryImpl
import com.upchiapas.infrastructure.adapters.TrackRepositoryImpl
import com.upchiapas.infrastructure.config.DatabaseFactory
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.http.*
import kotlinx.serialization.json.Json

/**
 * Punto de entrada de la aplicación
 */
fun main() {
    embeddedServer(Netty, port = 3000, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

/**
 * Módulo principal de configuración de Ktor
 */
fun Application.module() {
    // Inicializar base de datos
    DatabaseFactory.init()

    // Configurar serialización JSON
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }

    // Configurar CORS
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Post)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        anyHost()
    }

    // Manejo de errores global
    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError,
                mapOf("error" to (cause.message ?: "Error interno del servidor"))
            )
        }
    }

    // Inyección de dependencias manual (Hexagonal Architecture)
    val artistaRepository = ArtistaRepositoryImpl()
    val albumRepository = AlbumRepositoryImpl()
    val trackRepository = TrackRepositoryImpl()

    val artistaService = ArtistaService(artistaRepository)
    val albumService = AlbumService(albumRepository, artistaRepository)
    val trackService = TrackService(trackRepository, albumRepository)

    // Configurar rutas
    routing {
        route("/api") {
            artistaRoutes(artistaService)
            albumRoutes(albumService)
            trackRoutes(trackService)
        }

        // Health check endpoint
        get("/health") {
            call.respond(HttpStatusCode.OK, mapOf("status" to "UP"))
        }
    }
}