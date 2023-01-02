package schichtplanhgl.interactor.login

import schichtplanhgl.model.Login
import schichtplanhgl.model.Password
import schichtplanhgl.service.token.TokenService
import schichtplanhgl.service.user.UserService

class AuthInteractorImpl(
    private val userService: UserService,
    private val tokenService: TokenService
) : AuthInteractor {

    override fun areCredentialsValid(login: Login, password: Password) =
        userService.areCredentialsValid(login, password)

    override fun generateToken(login: Login, password: Password) =
        tokenService.generateToken(login)

    override fun generateRefreshToken(login: Login) =
        tokenService.generateRefreshToken(login)

    override fun isRefreshTokenValid(refreshToken: String): Boolean =
        tokenService.isRefreshTokenValid(refreshToken)

    override fun refreshTokens(login: Login, refreshToken: String) =
        tokenService.generateToken(login) to tokenService.generateRefreshToken(login)
}