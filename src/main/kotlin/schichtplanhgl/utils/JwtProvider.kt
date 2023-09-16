package schichtplanhgl.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.Payload
import com.typesafe.config.ConfigFactory
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import schichtplanhgl.domain.exceptions.CredentialsInvalidException
import schichtplanhgl.domain.model.Login
import schichtplanhgl.domain.model.Role
import schichtplanhgl.domain.model.User
import schichtplanhgl.ext.getEmail
import schichtplanhgl.ext.getId
import schichtplanhgl.ext.getRole
import java.util.*

object JwtProvider {
    private const val validityInMs = 36_000_00 * 10 // 10 hours
    private val appConfig = HoconApplicationConfig(ConfigFactory.load())
    private val issuer: String = appConfig.property("jwt.issuer").getString()
    private val audience: String = appConfig.property("jwt.audience").getString()
    private val secret: String = appConfig.property("jwt.secret").getString()
    private val algorithm = Algorithm.HMAC256(secret)
    val userRealm = appConfig.property("jwt.realmUser").getString()
    val adminRealm = appConfig.property("jwt.realmAdmin").getString()


    fun getVerifier(): JWTVerifier = JWT
        .require(algorithm)
        .withAudience(audience)
        .withIssuer(issuer)
        .build()


    fun createJWT(user: User): String =
        JWT.create()
            .withIssuedAt(Date())
            .withSubject("Authentication")
            .withIssuer(issuer)
            .withAudience(audience)
            .withClaim("id", user.id)
            .withClaim("email", user.email)
            .withClaim("role", user.role.name)
            .withExpiresAt(Date(System.currentTimeMillis() + validityInMs)).sign(Cipher.algorithm)

    fun decodeJWT(credential: JWTPrincipal?): Login {
        return credential?.let {
            Login(
                id = it.getId(),
                email = it.getEmail(),
                role = it.getRole()
            )
        }?: throw CredentialsInvalidException()
    }

    fun validateCredentialsUser(credential: JWTCredential): Principal? {
        val claims = credential.payload

        if (!validateBasic(claims)) return null

        // Check that the token contains the expected role claim.
        val role = claims.getClaim("role")?.asString()
        if (role == null || role !in Role.values().map { it.name }) {
            return null
        }

        return JWTPrincipal(claims)
    }

    fun validateCredentialsAdmin(credential: JWTCredential): Principal? {
        val claims = credential.payload

        validateBasic(claims)

        // Check that the token contains the expected role claim.
        val role = claims.getClaim("role")?.asString()
        if (role == null || role != Role.ADMIN.name || role != Role.HEIMLEITUNG.name) {
            return null
        }


        return JWTPrincipal(claims)
    }

    private fun validateBasic(claims: Payload): Boolean {

        // Check that the token is not expired.
        if (claims.expiresAt < Date()) {
            return false
        }

        // Check that the token is issued by the expected issuer.
        if (claims.issuer != issuer) {
            return false
        }

        // Check that the token is intended for the expected audience.
        if (!claims.audience.contains(audience)) {
            return false
        }

        claims.getClaim("email")?.asString() ?: return false

        return true
    }
}
