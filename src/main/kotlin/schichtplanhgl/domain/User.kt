package schichtplanhgl.domain

import io.ktor.server.auth.*
import kotlinx.serialization.Serializable
import schichtplanhgl.ext.isEmailValid

@Serializable
data class UserDTO(
    val userId: Long? = null,
    val email: String,
    val token: String? = null,
    val username: String? = "",
    val password: String,
    val role: Role? = Role.MITARBEITER,
    val activated: Boolean? = false
) {
    fun validRegister() = email.isEmailValid() && password.isNotBlank() && !username.isNullOrBlank()

    fun validLogin() = email.isEmailValid() && password.isNotBlank()

    fun validUpToDate() = email.isEmailValid() && password.isNotBlank() && !username.isNullOrBlank()
}

data class User(
    val id: Long,
    val email: String,
    val token: String? = null,
    val username: String,
    val password: String,
    val role: Role,
    val activated: Boolean = false
) : Principal


fun User.toDto() = UserDTO(
    userId = id,
    email = this.email,
    token = this.token,
    username = this.username,
    password = this.password,
    role = this.role,
    activated = this.activated
)