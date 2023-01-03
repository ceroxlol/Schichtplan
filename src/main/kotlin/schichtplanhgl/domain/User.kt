package schichtplanhgl.domain

import io.ktor.server.auth.*
import schichtplanhgl.ext.isEmailValid

data class UserDTO(val user: User? = null) {
    fun validRegister(): User {
        require(
            user != null &&
                user.email.isEmailValid() &&
                !user.password.isNullOrBlank() &&
                !user.username.isNullOrBlank()
        ) { "User is invalid." }
        return user
    }

    fun validLogin(): User {
        require(
            user != null &&
                user.email.isEmailValid() &&
                !user.password.isNullOrBlank()
        ) { "Email or password is invalid." }
        return user
    }

    fun validToUpdate(): User {
        require(
            user != null &&
                user.email.isEmailValid() &&
                !user.password.isNullOrBlank() &&
                !user.username.isNullOrBlank()
        ) { "User is invalid." }
        return user
    }
}

data class User(
    val id: Long? = null,
    val email: String,
    val token: String? = null,
    val username: String? = null,
    val password: String? = null,
    val role: Role? = null
) : Principal
