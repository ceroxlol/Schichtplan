package schichtplanhgl.user

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val password: String
) : Principal
