package schichtplanhgl.web

import io.ktor.server.auth.*
import io.ktor.server.routing.*
import schichtplanhgl.web.controllers.UserController

fun Routing.users(userController: UserController) {
    route("users") {
        post("register") { userController.register(this.context) }
        post("login") { userController.login(this.context) }
    }
    route("user") {
        authenticate {
            get { userController.getCurrent(this.context) }
            put { userController.update(this.context) }
        }
    }
}
