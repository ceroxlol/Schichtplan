package schichtplanhgl.web.controllers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import schichtplanhgl.domain.User
import schichtplanhgl.domain.UserDto
import schichtplanhgl.domain.service.UserService
import schichtplanhgl.domain.toDto

class UserController(private val userService: UserService) {
    suspend fun login(ctx: ApplicationCall) {
        ctx.receive<UserDto>().apply {
            if (this.validLogin()) {
                ctx.respond(userService.authenticate(this).toDto())
            } else {
                ctx.respond(HttpStatusCode.Unauthorized, "Login invalid.")
            }
        }
    }

    suspend fun register(ctx: ApplicationCall) {
        ctx.receive<UserDto>().apply {
            if(this.validRegister()){
                ctx.respond(userService.create(this).toDto())
            } else {
                ctx.respond(HttpStatusCode.BadRequest, "Couldn't register user.")
            }
        }
    }

    fun getUserByEmail(email: String?): User {
        return email.let {
            require(!it.isNullOrBlank()) { "User not logged or with invalid email." }
            userService.getByEmail(it)
        }
    }

    suspend fun getUserById(id: Long, ctx: ApplicationCall ) {
        userService.getById(id)
            .also {
                ctx.respond(it.toDto() )
            }
    }

    suspend fun getCurrent(ctx: ApplicationCall) {
        ctx.respond(ctx.authentication.principal<User>()!!.toDto())
    }

/*    suspend fun update(ctx: ApplicationCall) {
        val email = ctx.authentication.principal<User>()?.email
        require(!email.isNullOrBlank()) { "User not logged in." }
        ctx.receive<UserDTO>().also { userDto ->
            userService.update(email, userDto.validToUpdate()).apply {
                ctx.respond(UserDTO(this))
            }
        }
    }*/

    suspend fun getAll(ctx: ApplicationCall) {
        ctx.respond(userService.getAll().map { it.toDto() })
    }

    suspend fun activateUser(ctx: ApplicationCall) {
        ctx.receive<Long>().apply {
            userService.activateUser(this).also { ctx.respond(HttpStatusCode.OK) }
        }
    }

}
