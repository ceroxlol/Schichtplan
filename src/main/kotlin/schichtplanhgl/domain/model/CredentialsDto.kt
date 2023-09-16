package schichtplanhgl.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CredentialsDto(
    val email: String,
    val password: String
)