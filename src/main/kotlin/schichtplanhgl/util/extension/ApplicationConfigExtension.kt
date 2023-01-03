package schichtplanhgl.util.extension

import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.config.*
import schichtplanhgl.model.Milliseconds

private const val ALGORITHM_PATH = "jwt.SECRET"
private const val ISSUER_PATH = "jwt.ISSUER"
private const val VALIDITY_PATH = "jwt.VALIDITY_MS"
private const val REALM_PATH = "jwt.REALM"

fun ApplicationConfig.stringProperty(path: String) = property(path).getString()

fun ApplicationConfig.longProperty(path: String) = property(path).getString().toLong()

val ApplicationConfig.algorithm: Algorithm
    get() = Algorithm.HMAC512(stringProperty(ALGORITHM_PATH))

val ApplicationConfig.issuer
    get() = stringProperty(ISSUER_PATH)

val ApplicationConfig.validity
    get() = Milliseconds(longProperty(VALIDITY_PATH))

val ApplicationConfig.realm
    get() = stringProperty(REALM_PATH)