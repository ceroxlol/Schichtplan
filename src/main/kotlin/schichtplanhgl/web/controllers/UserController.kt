package schichtplanhgl.web.controllers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.util.logging.*
import schichtplanhgl.domain.exceptions.UserDataInvalid
import schichtplanhgl.domain.model.CredentialsDto
import schichtplanhgl.domain.model.UserDto
import schichtplanhgl.domain.model.toDto
import schichtplanhgl.domain.model.toUser
import schichtplanhgl.domain.service.UserService
import schichtplanhgl.ext.getEmail
import schichtplanhgl.ext.isAdminOrHeimleitung
import schichtplanhgl.ext.isEmailValid
import schichtplanhgl.utils.Cipher
import schichtplanhgl.utils.JwtProvider
import schichtplanhgl.utils.JwtProvider.decodeJWT
import java.util.*

internal val LOGGER = KtorSimpleLogger("UserController")

class UserController(private val userService: UserService) {

    private val base64Encoder = Base64.getEncoder()

    // NONE
    suspend fun login(ctx: ApplicationCall) {
        ctx.receive<CredentialsDto>().apply {
            if (email.isEmailValid()) {
                try {
                    val user = userService.getByEmail(email)
                    if (String(base64Encoder.encode(Cipher.encrypt(password))) == user.password) {
                        LOGGER.info("User ${user.email} logged in.")
                        ctx.respond(mapOf( "token" to JwtProvider.createJWT(user)))
                    } else {
                        LOGGER.error("User ${user.email} tried to log in with wrong password.")
                        ctx.respond(HttpStatusCode.BadRequest, "Credentials invalid.")
                    }
                } catch (e: Exception) {
                    LOGGER.error("User with email $email not found.")
                    ctx.respond(HttpStatusCode.BadRequest, "No user by this email $email.")
                }
            }
        }
    }

    // ADMIN
    suspend fun register(ctx: ApplicationCall) {
        with(ctx){
            val login = decodeJWT(authentication.principal<JWTPrincipal>())
            receive<UserDto>().apply {
                if (this.validRegister() && login.isAdminOrHeimleitung()) {
                    respond(HttpStatusCode.Created)
                } else {
                    respond(HttpStatusCode.BadRequest, "Couldn't register user.")
                }
            }
        }
    }

    // USER
    suspend fun getUserById(id: Long, ctx: ApplicationCall) {
        with(ctx) {
            val login = decodeJWT(authentication.principal<JWTPrincipal>())
            if(login.isAdminOrHeimleitung() || login.id == id) {
                userService.getById(id).also { respond(it.toDto()) }
            } else {
                ctx.respond(HttpStatusCode.Forbidden, "Not allowed to change user with the id $id")
            }
        }
    }

    // USER
    suspend fun getCurrent(ctx: ApplicationCall) {
        ctx.authentication.principal<JWTPrincipal>()?.getEmail()
            ?.let { userService.getByEmail(it).toDto() }
            ?: ctx.respond(HttpStatusCode.BadRequest, "No user found.")
    }

    // USER
    suspend fun upsert(ctx: ApplicationCall) {
        ctx.receive<UserDto>().apply {
            val login = decodeJWT(ctx.authentication.principal<JWTPrincipal>())
            if(!this.valid()){
                throw UserDataInvalid()
            } else if (this.id == null && login.isAdminOrHeimleitung()) {
                userService.createUser(this).apply {
                    ctx.respond(this.toDto())
                }
            } else if(this.id == login.id){
                userService.update(this.toUser()).apply {
                    ctx.respond(this?.toDto() ?: "No User found with this ID.")
                }
            } else{
                ctx.respond(HttpStatusCode.BadRequest, "Can't insert or update user.")
            }
        }
    }

    // ADMIN
    suspend fun getAllUsers(ctx: ApplicationCall) {
        ctx.respond(userService.getAll().map { it.toDto() })
    }

    // ADMIN
    suspend fun activateUser(ctx: ApplicationCall) {
        ctx.receive<Long>().apply {
            userService.activateUser(this).also { ctx.respond(HttpStatusCode.OK) }
        }
    }

}
