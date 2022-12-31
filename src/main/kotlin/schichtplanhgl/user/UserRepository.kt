package schichtplanhgl.user

import io.ktor.server.auth.*


interface UserRepository {
    fun findUserById(id: Int): User

    fun findUserByCredentials(credential: UserPasswordCredential): User
}