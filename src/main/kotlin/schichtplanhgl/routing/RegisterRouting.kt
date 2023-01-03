package schichtplanhgl.routing

import io.ktor.server.application.*
import io.ktor.server.locations.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.post
import schichtplanhgl.controller.register.RegisterController
import schichtplanhgl.model.Login
import schichtplanhgl.model.Name
import schichtplanhgl.model.Password

@KtorExperimentalLocationsAPI
fun Routing.register(controller: RegisterController) {

    @Location("/register")
    data class PostRegister(val login: String, val password: String, val name: String)

    post<PostRegister> { registerModel ->
        call.respond(
            controller.register(Login(registerModel.login), Password(registerModel.password), Name(registerModel.name))
        )
    }
}