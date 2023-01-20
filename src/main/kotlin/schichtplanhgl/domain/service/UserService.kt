package schichtplanhgl.domain.service

import schichtplanhgl.domain.User
import schichtplanhgl.domain.exceptions.NotFoundException
import schichtplanhgl.domain.exceptions.UnauthorizedException
import schichtplanhgl.domain.repository.UserRepository
import schichtplanhgl.utils.Cipher
import schichtplanhgl.utils.JwtProvider
import java.util.*

class UserService(private val jwtProvider: JwtProvider, private val userRepository: UserRepository) {
    private val base64Encoder = Base64.getEncoder()

    fun create(user: User): User {
        userRepository.findByEmail(user.email).apply {
            require(this == null) { "Email already registered!" }
        }
        val id = userRepository.create(user.copy(password = String(base64Encoder.encode(Cipher.encrypt(user.password)))))
        return user.copy(id = id, token = generateJwtToken(user))
    }

    fun authenticate(user: User): User {
        val userFound = userRepository.findByEmail(user.email)
        if (userFound?.password == String(base64Encoder.encode(Cipher.encrypt(user.password)))) {
            return userFound.copy(token = generateJwtToken(userFound))
        }
        throw UnauthorizedException("email or password invalid!")
    }

    fun getByEmail(email: String): User {
        val user = userRepository.findByEmail(email)
        user ?: throw NotFoundException("User with email '$email' not found.")
        return user.copy(token = generateJwtToken(user))
    }

    fun update(email: String, user: User): User? {
        return userRepository.update(email, user)
    }

    fun getAll(): List<User> = userRepository.findAll()

    fun activateUser(id: Long) = userRepository.activate(id)

    private fun generateJwtToken(user: User): String? {
        return jwtProvider.createJWT(user)
    }
}
