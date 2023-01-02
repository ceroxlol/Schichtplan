package schichtplanhgl.user

import io.ktor.server.auth.*
import schichtplanhgl.model.User


interface UserRepository {
    fun findUserById(id: Int): User

    fun findUserByCredentials(credential: UserPasswordCredential): User
}