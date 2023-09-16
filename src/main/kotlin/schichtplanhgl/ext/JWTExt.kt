package schichtplanhgl.ext

import io.ktor.server.auth.jwt.*
import schichtplanhgl.domain.exceptions.CredentialsInvalidException
import schichtplanhgl.domain.model.Role

fun JWTPrincipal.getEmail() = this.payload.getClaim("email")?.asString() ?: throw CredentialsInvalidException()

fun JWTPrincipal.getId() = this.payload.getClaim("id")?.asLong() ?: throw CredentialsInvalidException()

fun JWTPrincipal.getRole() = this.payload.getClaim("role")?.asString()?.let { Role.valueOf(it) } ?: throw CredentialsInvalidException()