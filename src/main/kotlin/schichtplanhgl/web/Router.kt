package schichtplanhgl.web

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import schichtplanhgl.web.controllers.ShiftController
import schichtplanhgl.web.controllers.UserController

fun Routing.users(userController: UserController) {
    route("api/users") {
        post("register") { userController.register(this.context) }
        post("login") { userController.login(this.context) }
        authenticate {
            get("all") { userController.getAll(this.context) }
            get("{userId}") { userController.getUserById(call.parameters.getOrFail("userId").toLong(), this.context) }
            post("{userId}") { userController.upsert(this.context)}
        }
    }
    route("user") {
        authenticate {
            get { userController.getCurrent(this.context) }
            //put { userController.update(this.context) }
            post("activate") { userController.activateUser(this.context) }
        }
    }
}

fun Routing.shifts(shiftController: ShiftController) {
    route("api/shifts") {
        authenticate {
            get { shiftController.getAll(this.context) }
            get("{userId}") { shiftController.getShiftsByUserId(call.parameters.getOrFail("userId").toLong(), this.context) }
            post { shiftController.createShift(this.context) }
            delete("{shiftId}"){ shiftController.deleteShift(call.parameters.getOrFail("shiftId").toLong(), this.context) }
        }
    }
}