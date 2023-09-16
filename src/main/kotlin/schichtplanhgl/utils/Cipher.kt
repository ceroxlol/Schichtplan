package schichtplanhgl.utils

import com.auth0.jwt.algorithms.Algorithm

object Cipher {
    // TODO: Change this to a more secure value
    val algorithm: Algorithm = Algorithm.HMAC256("something-very-secret-here")

    fun encrypt(data: String?): ByteArray = algorithm.sign(data?.toByteArray())
}
