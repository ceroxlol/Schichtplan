package schichtplanhgl.controller.register

import schichtplanhgl.model.Login
import schichtplanhgl.model.Name
import schichtplanhgl.model.Password
import schichtplanhgl.response.Response

interface RegisterController {

    fun register(login: Login, password: Password, name: Name): Response
}