package schichtplanhgl.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

object DbConfig {
    fun setup(jdbcUrl: String, username: String, password: String) {
        val config = HikariConfig().also { config ->
            config.jdbcUrl = jdbcUrl
            config.username = username
            config.password = password
            //config.driverClassName = "org.h2.Driver"
            config.driverClassName = "org.postgresql.Driver"
        }
        Database.connect(HikariDataSource(config))
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
