package schichtplanhgl.interactor.register

import schichtplanhgl.model.Login
import schichtplanhgl.model.Name
import schichtplanhgl.model.Password

interface RegisterInteractor {

    fun doesUserExists(login: Login): Boolean

    fun registerUser(login: Login, password: Password, name: Name)
}