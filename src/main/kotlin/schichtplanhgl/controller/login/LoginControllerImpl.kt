package schichtplanhgl.controller.login

import schichtplanhgl.controller.base.BaseController
import schichtplanhgl.interactor.login.AuthInteractor
import schichtplanhgl.model.Login
import schichtplanhgl.model.Password
import schichtplanhgl.response.Response
import schichtplanhgl.response.badRequest
import schichtplanhgl.response.error.ErrorResponseBody
import schichtplanhgl.response.ok
import schichtplanhgl.response.success.LoginResponseBody
import schichtplanhgl.response.success.RefreshTokenResponseBody
import schichtplanhgl.response.unauthorized
import schichtplanhgl.validation.InputValidator

class LoginControllerImpl (
    private val interactor: AuthInteractor,
    private val inputValidator: InputValidator
) : BaseController(), LoginController {

    override suspend fun login(login: Login, password: Password) = when {
        isInputValid(login, password).not() ->
            Response.badRequest(ErrorResponseBody.InputInvalid)

        userExistsAndPasswordIsValid(login, password).not() ->
            Response.unauthorized(ErrorResponseBody.AuthorizationFailed)

        else -> {
            val (token, refreshToken) = loginUser(login, password)
            Response.ok(LoginResponseBody(token, refreshToken))
        }
    }

    private fun isInputValid(login: Login, password: Password) =
        inputValidator.run { login.isValid() && password.isValid() }

    private fun userExistsAndPasswordIsValid(login: Login, password: Password) =
        interactor.areCredentialsValid(login, password)

    private fun loginUser(login: Login, password: Password) =
        interactor.generateToken(login, password) to interactor.generateRefreshToken(login)

    override suspend fun refreshToken(login: Login, refreshToken: String) = when {
        isRefreshTokenValid(refreshToken).not() ->
            Response.unauthorized(ErrorResponseBody.AuthorizationFailed)

        else -> {
            val (newToken, newRefreshToken) = generateNewTokens(login, refreshToken)
            Response.ok(RefreshTokenResponseBody(newToken, newRefreshToken))
        }
    }

    private fun isRefreshTokenValid(refreshToken: String): Boolean =
        interactor.isRefreshTokenValid(refreshToken)

    private fun generateNewTokens(login: Login, refreshToken: String): Pair<String, String> =
        interactor.refreshTokens(login, refreshToken)
}