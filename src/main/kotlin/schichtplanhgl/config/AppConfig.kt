package schichtplanhgl.config

import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.kodein.di.direct
import org.kodein.di.instance
import org.slf4j.event.Level
import schichtplanhgl.utils.JwtProvider
import schichtplanhgl.web.controllers.ShiftController
import schichtplanhgl.web.controllers.UserController
import schichtplanhgl.web.shifts
import schichtplanhgl.web.users

fun setup(): BaseApplicationEngine {

    val username = System.getenv("POSTGRES_USER") ?: "postgres"
    val password = System.getenv("POSTGRES_PASSWORD") ?: "postgres"
    val host = System.getenv("POSTGRES_HOST") ?: "localhost"
    val port = System.getenv("POSTGRES_PORT") ?: "5432"
    val jdbcUrl = "jdbc:postgresql://$host:$port/schichtplan?user=$username"
    DbConfig.setup(jdbcUrl, username, password)

    return server(Netty)
}

fun server(
    engine: ApplicationEngineFactory<BaseApplicationEngine,
            out ApplicationEngine.Configuration>
): BaseApplicationEngine {
    return embeddedServer(
        engine,
        port = (System.getenv("PORT") ?: "8080").toInt(),
        watchPaths = listOf("mainModule"),
        module = Application::mainModule
    )
}

fun Application.mainModule() {
    val userController = ModulesConfig.di.direct.instance<UserController>()
    val shiftController = ModulesConfig.di.direct.instance<ShiftController>()

    install(CallLogging) {
        level = Level.DEBUG
    }
    install(ContentNegotiation) {
        json(
            Json {
                prettyPrint = true
                encodeDefaults = true
            }
        )
    }
    install(Authentication) {
        jwt {
            verifier(JwtProvider.verifier)
            authSchemes("Token")
            validate { credential ->
                if (credential.payload.audience.contains(JwtProvider.audience)) {
                    userController.getUserByEmail(credential.payload.claims["email"]?.asString())
                } else null
            }
            challenge{_, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Credentials are not valid")
            }
        }
    }
    install(StatusPages) {
        exception<ContentTransformationException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, "Failed to parse request: ${cause.message}")
        }
/*        exception(Exception::class.java) {
            val errorResponse = ErrorResponse(mapOf("error" to listOf("detail", this.toString())))
            context.respond(
                HttpStatusCode.InternalServerError, errorResponse
            )
        }*/
    }
    // for local development
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHost("localhost:3000")
    }
    install(Routing) {
        users(userController)
        shifts(shiftController)
    }
}
