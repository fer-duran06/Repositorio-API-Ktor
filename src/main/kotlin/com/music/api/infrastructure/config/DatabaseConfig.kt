package com.music.api.infrastructure.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database

object DatabaseConfig {

    fun init(config: ApplicationConfig) {
        // Valores por defecto si no encuentra la configuración
        val jdbcUrl = try {
            config.property("database.jdbcUrl").getString()
        } catch (e: Exception) {
            println("No se encontró database.jdbcUrl, usando valor por defecto")
            "jdbc:postgresql://localhost:5432/musicapp"
        }

        val driver = try {
            config.property("database.driver").getString()
        } catch (e: Exception) {
            "org.postgresql.Driver"
        }

        val user = try {
            config.property("database.user").getString()
        } catch (e: Exception) {
            "postgres"
        }

        val password = try {
            config.property("database.password").getString()
        } catch (e: Exception) {
            "fer123"
        }

        val maxPoolSize = try {
            config.property("database.maxPoolSize").getString().toInt()
        } catch (e: Exception) {
            10
        }

        println("Conectando a: $jdbcUrl")

        val hikariConfig = HikariConfig().apply {
            this.jdbcUrl = jdbcUrl
            this.driverClassName = driver
            this.username = user
            this.password = password
            this.maximumPoolSize = maxPoolSize
            this.isAutoCommit = false
            this.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        val dataSource = HikariDataSource(hikariConfig)
        Database.connect(dataSource)
    }
}