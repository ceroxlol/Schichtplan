package schichtplanhgl.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*

//const val audience = "http://0.0.0.0:8080/hello"
//const val issuer = "http://0.0.0.0:8080/"

fun Application.configureSecurity() {

    install(ContentNegotiation) { json() }

    authentication {
        jwt("auth-jwt") {
            realm = "Access to 'hello'"
            verifier(
                JWT
                    .require(Algorithm.HMAC256("secret"))
                    //.withAudience(audience)
                    //.withIssuer(issuer)
                    .build()
            )
            validate { credential ->
                //if (credential.payload.audience.contains(audience)) JWTPrincipal(credential.payload) else null
                null
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}
