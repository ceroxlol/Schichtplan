package schichtplanhgl.service.token

import schichtplanhgl.model.Login

interface TokenService {

    fun generateToken(login: Login): String

    fun generateRefreshToken(login: Login): String

    fun isRefreshTokenValid(refreshToken: String): Boolean
}