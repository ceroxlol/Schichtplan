package schichtplanhgl.domain.service

import io.ktor.util.logging.*
import schichtplanhgl.domain.model.Role
import schichtplanhgl.domain.model.User
import schichtplanhgl.domain.model.UserDto
import schichtplanhgl.domain.exceptions.NotFoundException
import schichtplanhgl.domain.repository.UserRepository
import schichtplanhgl.utils.Cipher
import java.util.*

internal val LOGGER = KtorSimpleLogger("UsersService")

class UserService(private val userRepository: UserRepository) {
    private val base64Encoder = Base64.getEncoder()

    fun createUser(userDTO: UserDto): User {
        userRepository.findByEmail(userDTO.email)?.let { user ->
            LOGGER.warn("User with email '${userDTO.email}' already exists.")
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

        return userRepository.findById(id)!!
    }

    @Throws(NotFoundException::class)
    fun getByEmail(email: String) =
        userRepository.findByEmail(email) ?: throw NotFoundException("User with email '$email' not found.")

    @Throws(NotFoundException::class)
    fun getById(id: Long) = userRepository.findById(id) ?: throw NotFoundException("User with email '$id' not found.")

    fun update(user: User) = userRepository.update(user)

    fun getAll(): List<User> = userRepository.findAll()

    fun activateUser(id: Long) = userRepository.activate(id)
}
