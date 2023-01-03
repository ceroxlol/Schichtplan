package schichtplanhgl.controller.register

import schichtplanhgl.response.error.ErrorResponseBody

sealed class RegisterErrorResponseBody(message: String) : ErrorResponseBody(message) {

    object LoginAlreadyTaken : RegisterErrorResponseBody("LoginAlreadyTaken")
}