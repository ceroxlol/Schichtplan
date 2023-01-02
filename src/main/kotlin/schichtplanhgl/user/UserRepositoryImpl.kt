package schichtplanhgl.user

import io.ktor.server.auth.*
import schichtplanhgl.model.User
import schichtplanhgl.testUser

class UserRepositoryImpl : UserRepository {

    override fun findUserById(id: Int): User = users.getValue(id)

    override fun findUserByCredentials(credential: UserPasswordCredential): User = testUser

    private val users = listOf(testUser).associateBy(User::id)

}