package schichtplanhgl.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Login (
    val id: Long,
    val email: String,
    val role: Role
)