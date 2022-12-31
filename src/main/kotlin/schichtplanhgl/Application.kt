package schichtplanhgl

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import schichtplanhgl.plugins.*

const val port = 8080
const val host = "0.0.0.0"
val module = Application::module

fun main() {
    embeddedServer(
        Netty,
        port = port,
        host = host,
        module = module
    ).also {
        print("Started. Listening on $port...")
    }
        .start(wait = true)
}

fun Application.module() {
    configureSecurity()
    configureMonitoring()
    configureRouting()
}
