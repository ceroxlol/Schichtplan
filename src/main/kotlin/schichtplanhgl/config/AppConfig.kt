package schichtplanhgl.config

import io.ktor.http.*
import io.ktor.serialization.jackson.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.kodein.di.direct
import org.kodein.di.instance
import schichtplanhgl.utils.JwtProvider
import schichtplanhgl.web.ErrorResponse
import schichtplanhgl.web.controllers.UserController
import schichtplanhgl.web.users

const val SERVER_PORT = 8080


fun setup(): BaseApplicationEngine {
    //DbConfig.setup("jdbc:h2:mem:DATABASE_TO_UPPER=false;", "sa", "")
    DbConfig.setup(
        "jdbc:h2:file:~/schichtplan/schichtplan/schichtplan;AUTO_SERVER=TRUE;DATABASE_TO_UPPER=false;",
        "sa",
        ""
    )
    return server(Netty)
}

fun server(
    engine: ApplicationEngineFactory<BaseApplicationEngine,
            out ApplicationEngine.Configuration>
): BaseApplicationEngine {
    return embeddedServer(
        engine,
        port = SERVER_PORT,
        watchPaths = listOf("mainModule"),
        module = Application::mainModule
    )
}

fun Application.mainModule() {
    val userController = ModulesConfig.di.direct.instance<UserController>()

    install(CallLogging)
    install(ContentNegotiation) {
        jackson {}
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
        }
    }
    install(StatusPages) {
        exception(Exception::class.java) {
            val errorResponse = ErrorResponse(mapOf("error" to listOf("detail", this.toString())))
            context.respond(
                HttpStatusCode.InternalServerError, errorResponse
            )
        }
    }

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
    }
}
