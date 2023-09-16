package schichtplanhgl.web

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import schichtplanhgl.web.controllers.ShiftController
import schichtplanhgl.web.controllers.UserController

fun Routing.users(userController: UserController) {
    route("api/users") {
        post("login") { userController.login(this.context) }

        authenticate("admin") {
            post("register") { userController.register(this.context) }
            get("all") { userController.getAllUsers(this.context) }
        }

        authenticate("user") {
            get("{userId}") { userController.getUserById(call.parameters.getOrFail("userId").toLong(), this.context) }
            post("{userId}") { userController.upsert(this.context) }
        }
    }
    route("user") {
        authenticate("user") {
            get { userController.getCurrent(this.context) }
        }
        authenticate("admin") {
            //put { userController.update(this.context) }
            post("activate") { userController.activateUser(this.context) }
        }
    }
}

fun Routing.shifts(shiftController: ShiftController) {
    route("api/shifts") {
        authenticate("user") {
            get { shiftController.getAll(this.context) }
            get("{userId}") {
                shiftController.getShiftsByUserId(
                    call.parameters.getOrFail("userId").toLong(),
                    this.context
                )
            }
            post { shiftController.upsertShift(this.context) }
            delete("{shiftId}") {
                shiftController.deleteShift(
                    call.parameters.getOrFail("shiftId").toLong(),
                    this.context
                )
            }
        }
    }
}