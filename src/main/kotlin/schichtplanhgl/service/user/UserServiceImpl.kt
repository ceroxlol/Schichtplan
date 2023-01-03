package schichtplanhgl.service.user

import org.jetbrains.exposed.sql.transactions.transaction
import schichtplanhgl.db.tables.user.User
import schichtplanhgl.db.tables.user.UserTile
import schichtplanhgl.db.tables.user.Users
import schichtplanhgl.db.tables.user.toUserTile
import schichtplanhgl.model.Login
import schichtplanhgl.model.Password
import schichtplanhgl.repository.hash.HashRepository

class UserServiceImpl(
    private val hashRepository: HashRepository
) : UserService {

    override fun areCredentialsValid(login: Login, password: Password) =
        userByLoginOrNull(login)?.password?.value.let {
            it != null && it == password.hash()
        }

    override fun userByLoginOrNull(login: Login): UserTile? =
        transaction {
            User.find { Users.login eq login.value }
                .firstOrNull()
                ?.toUserTile()
        }

    override fun createUser(userTile: UserTile) {
        transaction {
            User.new {
                login = userTile.login.value
                name = userTile.name.value
                password = userTile.password.hash()
            }
        }
    }

    override fun doesUserExist(login: Login) =
        transaction { User.find { Users.login eq login.value }.empty().not() }

    private fun Password.hash() = hashRepository.hash(this.value)
}