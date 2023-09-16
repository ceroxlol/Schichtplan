package schichtplanhgl.utils

import java.util.Random
object PasswordGenerator {

    fun generateRandomPassword(length: Int): String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        val random = Random()
        val password = StringBuilder()

        for (i in 1..length) {
            val index = random.nextInt(chars.length)
            password.append(chars[index])
        }

        return password.toString()
    }
}
