package schichtplanhgl.interactor.login

import schichtplanhgl.model.Login
import schichtplanhgl.model.Password

interface AuthInteractor {

    fun areCredentialsValid(login: Login, password: Password): Boolean

    fun generateToken(login: Login, password: Password): String

    fun generateRefreshToken(login: Login): String

    fun isRefreshTokenValid(refreshToken: String): Boolean

    fun refreshTokens(login: Login, refreshToken: String): Pair<String, String>
}