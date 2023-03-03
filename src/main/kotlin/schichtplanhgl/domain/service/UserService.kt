package schichtplanhgl.domain.service

import io.ktor.server.engine.*
import schichtplanhgl.domain.Role
import schichtplanhgl.domain.User
import schichtplanhgl.domain.UserDTO
import schichtplanhgl.domain.exceptions.NotFoundException
import schichtplanhgl.domain.exceptions.UnauthorizedException
import schichtplanhgl.domain.repository.UserRepository
import schichtplanhgl.utils.Cipher
import schichtplanhgl.utils.JwtProvider
import java.util.*

class UserService(private val jwtProvider: JwtProvider, private val userRepository: UserRepository) {
    private val base64Encoder = Base64.getEncoder()

    fun create(userDTO: UserDTO): User {
        userRepository.findByEmail(userDTO.email)?.let { user ->
            applicationEngineEnvironment { log.warn("User with email ${userDTO.email} already exists.") }
            return user
        }
        val encodedPassword = String(base64Encoder.encode(Cipher.encrypt(userDTO.password)))
        val role = userDTO.role ?: Role.MITARBEITER
        val id = userRepository.create(
            email = userDTO.email,
            username = userDTO.username!!,
            password = encodedPassword,
            activated = false,
            role = role
        )
        //TODO: One should not be immediately authenticated
        return userRepository.findById(id)!!.copy(
            token = generateJwtToken(userDTO.email)
        )
    }

    fun authenticate(userDto: UserDTO): User {
        val user = userRepository.findByEmail(userDto.email)
        if (user?.password == String(base64Encoder.encode(Cipher.encrypt(userDto.password)))) {
            return user.copy(token = generateJwtToken(userEmail = userDto.email))
        }
        throw UnauthorizedException("email or password invalid!")
    }

    fun getByEmail(email: String): User {
        val user = userRepository.findByEmail(email)
        user ?: throw NotFoundException("User with email '$email' not found.")
        return user.copy(token = generateJwtToken(userEmail = user.email))
    }

    fun update(email: String, user: User): User? {
        return userRepository.update(email, user)
    }

    fun getAll(): List<User> = userRepository.findAll()

    fun activateUser(id: Long) = userRepository.activate(id)

    private fun generateJwtToken(userEmail : String): String? {
        return jwtProvider.createJWT(userEmail)
    }
}
