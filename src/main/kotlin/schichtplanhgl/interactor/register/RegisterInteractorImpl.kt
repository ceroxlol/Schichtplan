package schichtplanhgl.interactor.register

import schichtplanhgl.db.tables.user.UserTile
import schichtplanhgl.model.Login
import schichtplanhgl.model.Name
import schichtplanhgl.model.Password
import schichtplanhgl.service.user.UserService

class RegisterInteractorImpl(
    private val userService: UserService
) : RegisterInteractor {

    override fun doesUserExists(login: Login) =
        userService.doesUserExist(login)

    override fun registerUser(login: Login, password: Password, name: Name) =
        userService.createUser(UserTile(login, password, name))
}