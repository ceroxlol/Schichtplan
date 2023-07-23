package schichtplanhgl.web.controllers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import schichtplanhgl.domain.User
import schichtplanhgl.domain.UserDto
import schichtplanhgl.domain.exceptions.UnauthorizedException
import schichtplanhgl.domain.service.UserService
import schichtplanhgl.domain.toDto
import schichtplanhgl.domain.toUser

class UserController(private val userService: UserService) {

    private val logger: Logger = LoggerFactory.getLogger("UserController")

    suspend fun login(ctx: ApplicationCall) {
        ctx.receive<UserDto>().apply {
            // TODO Beautify this
            if (this.validLogin()) {
                try {
                    ctx.respond(userService.authenticate(this).toDto())
                } catch (e: UnauthorizedException) {
                    ctx.respond(HttpStatusCode.Unauthorized, "Credentials invalid.")
                }
            } else {
                ctx.respond(HttpStatusCode.BadRequest, "Missing credentials.")
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
                logger.info(it.toString())
                ctx.respond(it.toDto() )
                logger.info(it.toDto().toString())
            }
    }

    suspend fun getCurrent(ctx: ApplicationCall) {
        ctx.respond(ctx.authentication.principal<User>()!!.toDto())
    }

    suspend fun upsert(ctx: ApplicationCall) {
        ctx.receive<UserDto>().apply {
            require(this.valid()){ "Missing email, password or username"}
            if(this.id == null){
                userService.create(this).apply {
                    ctx.respond(this.toDto())
                }
            } else {
                userService.update(this.toUser()).apply {
                    ctx.respond(this?.toDto() ?: "No User found with this ID.")
                }
            }
        }
    }

    suspend fun getAll(ctx: ApplicationCall) {
        ctx.respond(userService.getAll().map { it.toDto() })
    }

    suspend fun activateUser(ctx: ApplicationCall) {
        ctx.receive<Long>().apply {
            userService.activateUser(this).also { ctx.respond(HttpStatusCode.OK) }
        }
    }

}
