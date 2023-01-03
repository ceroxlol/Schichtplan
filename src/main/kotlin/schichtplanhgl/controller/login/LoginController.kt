package schichtplanhgl.controller.login

import schichtplanhgl.model.Login
import schichtplanhgl.model.Password
import schichtplanhgl.response.Response

interface LoginController {

    suspend fun login(login: Login, password: Password): Response

    suspend fun refreshToken(login: Login, refreshToken: String): Response
}