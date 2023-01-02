package schichtplanhgl.service.user

import schichtplanhgl.model.Login
import schichtplanhgl.model.Password
import schichtplanhgl.db.tables.user.UserTile

interface UserService : UserExistenceService {

    fun areCredentialsValid(login: Login, password: Password): Boolean

    fun userByLoginOrNull(login: Login): UserTile?

    fun createUser(userTile: UserTile)
}