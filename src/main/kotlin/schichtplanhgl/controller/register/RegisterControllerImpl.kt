package schichtplanhgl.controller.register

import schichtplanhgl.controller.base.BaseController
import schichtplanhgl.interactor.register.RegisterInteractor
import schichtplanhgl.model.Login
import schichtplanhgl.model.Name
import schichtplanhgl.model.Password
import schichtplanhgl.response.Response
import schichtplanhgl.response.badRequest
import schichtplanhgl.response.created
import schichtplanhgl.response.error.ErrorResponseBody
import schichtplanhgl.response.success.RegisterResponseBody
import schichtplanhgl.validation.InputValidator

class RegisterControllerImpl(
    private val interactor: RegisterInteractor,
    private val inputValidator: InputValidator
) : BaseController(), RegisterController {

    override fun register(login: Login, password: Password, name: Name) =
        when {
            isInputValid(login, password, name).not() ->
                Response.badRequest(ErrorResponseBody.InputInvalid)

            doesUserExist(login) ->
                Response.badRequest(RegisterErrorResponseBody.LoginAlreadyTaken)

            else -> {
                registerUser(login, password, name)
                Response.created(RegisterResponseBody())
            }
        }

    private fun isInputValid(login: Login, password: Password, name: Name) =
        inputValidator.run { login.isValid() && name.isValid() && password.isValid() }

    private fun doesUserExist(login: Login) = interactor.doesUserExists(login)

    private fun registerUser(login: Login, password: Password, name: Name) =
        interactor.registerUser(login, password, name)
}