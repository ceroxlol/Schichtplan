package schichtplanhgl.web.controllers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import schichtplanhgl.domain.User
import schichtplanhgl.domain.UserDTO
import schichtplanhgl.domain.service.UserService

class UserController(private val userService: UserService) {
    suspend fun login(ctx: ApplicationCall) {
        ctx.receive<UserDTO>().apply {
            userService.authenticate(this.validLogin()).apply {
                ctx.respond(UserDTO(this))
            }
        }
    }

    suspend fun register(ctx: ApplicationCall) {
        ctx.receive<UserDTO>().apply {
            userService.create(this.validRegister()).apply {
                ctx.respond(UserDTO(this))
            }
        }
    }

    fun getUserByEmail(email: String?): User {
        return email.let {
            require(!it.isNullOrBlank()) { "User not logged or with invalid email." }
            userService.getByEmail(it)
        }
    }

    suspend fun getCurrent(ctx: ApplicationCall) {
        ctx.respond(UserDTO(ctx.authentication.principal()))
    }

    suspend fun update(ctx: ApplicationCall) {
        val email = ctx.authentication.principal<User>()?.email
        require(!email.isNullOrBlank()) { "User not logged in." }
        ctx.receive<UserDTO>().also { userDto ->
            userService.update(email, userDto.validToUpdate()).apply {
                ctx.respond(UserDTO(this))
            }
        }
    }

    suspend fun getAll(ctx: ApplicationCall) {
        ctx.receive<List<UserDTO>>()
    }

    suspend fun activateUser(ctx: ApplicationCall) {
        ctx.receive<Long>().apply {
            userService.activateUser(this).also { ctx.respond(HttpStatusCode.OK) }
        }
    }

}
