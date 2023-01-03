package schichtplanhgl.routing

import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.post
import schichtplanhgl.controller.login.LoginController
import schichtplanhgl.model.Login
import schichtplanhgl.model.Password

@KtorExperimentalLocationsAPI
fun Routing.login(controller: LoginController) {

    @Location("/login")
    data class PostLogin(val login: String, val password: String)

    post<PostLogin> { loginModel ->
        call.respond(
            controller.login(Login(loginModel.login), Password(loginModel.password))
        )
    }
}