package com.music.api.infrastructure.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.*
import org.jetbrains.exposed.sql.Database

object DatabaseConfig {
    
    fun init(config: ApplicationConfig) {
        val jdbcUrl = config.property("database.jdbcUrl").getString()
        val driver = config.property("database.driver").getString()
        val user = config.property("database.user").getString()
        val password = config.property("database.password").getString()
        val maxPoolSize = config.property("database.maxPoolSize").getString().toInt()
        
        val hikariConfig = HikariConfig().apply {
            jdbcUrl = jdbcUrl
            driverClassName = driver
            username = user
            this.password = password
            maximumPoolSize = maxPoolSize
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        
        val dataSource = HikariDataSource(hikariConfig)
        Database.connect(dataSource)
    }
}
