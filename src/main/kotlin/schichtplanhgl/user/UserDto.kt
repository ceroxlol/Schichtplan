package schichtplanhgl.user

import kotlinx.serialization.Serializable

@Serializable
data class UserDto(
    val username : String,
    val password : String
)