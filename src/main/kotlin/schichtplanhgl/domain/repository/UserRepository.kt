package schichtplanhgl.domain.repository

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import schichtplanhgl.domain.model.Role
import schichtplanhgl.domain.model.User
import schichtplanhgl.utils.Cipher
import schichtplanhgl.utils.PasswordGenerator.generateRandomPassword
import java.io.File
import java.util.*

internal object Users : LongIdTable() {
    val email: Column<String> = varchar("email", 200).uniqueIndex()
    val username: Column<String> = varchar("username", 100)
    val password: Column<String> = varchar("password", 150)
    val role: Column<Role> = enumerationByName("role", 50, Role::class).default(Role.MITARBEITER)
    val activated: Column<Boolean> = bool("activated")

    fun toDomain(row: ResultRow): User {
        return User(
            id = row[id].value,
            email = row[email],
            username = row[username],
            password = row[password],
            role = row[role],
            activated = row[activated]
        )
    }
}

class UserRepository {

    private val adminEmail = System.getenv("ADMIN_EMAIL") ?: "christopher.werner1@gmx.net"

    init {
        transaction {
            SchemaUtils.create(Users)

        }
/*        val user = User(
            id = 2,
            email = "test1@test.de",
            username = "Testuser1",
            password = "Testpasswort",
            role = Role.MITARBEITER
        )
        create(user)

        create(user.copy(id = 3, email = "test2@test.de", username = "Testuser2"))
        create(user.copy(id = 4, email = "test3@test.de", username = "Testuser3"))*/
/*        if(findByEmail("christopher.werner1@gmx.net") == null) {
            create(
                User(
                    id = 1,
                    email = "christopher.werner1@gmx.net",
                    username = "Christopher",
                    password = String(Base64.getEncoder().encode(Cipher.encrypt("abc123"))),
                    role = Role.ADMIN,
                    activated = true
                )
            )
        }*/
        createDefaultAdminUser()
    }

    fun findByEmail(email: String): User? {
        return transaction {
            Users.select { Users.email eq email }
                .map { Users.toDomain(it) }
                .firstOrNull()
        }
    }

    fun findById(id: Long): User?{
        return transaction {
            Users.select { Users.id eq id }
                .map { Users.toDomain(it) }
                .firstOrNull()
        }
    }

    fun create(user: User): Long {
        return transaction {
            Users.insertAndGetId { row ->
                row[email] = user.email
                row[username] = user.username
                row[password] = user.password
                row[activated] = user.activated
            }.value
        }
    }

    fun create(email: String, username: String, password: String, activated: Boolean, role: Role): Long {
        return transaction {
            Users.insertAndGetId { row ->
                row[Users.email] = email
                row[Users.username] = username
                row[Users.password] = password
                row[Users.activated] = activated
                row[Users.role] = role
            }.value
        }
    }

    fun update(user: User): User? {
        transaction {
            Users.update({ Users.id eq user.id }) { row ->
                row[email] = user.email
                row[username] = user.username
                row[password] = user.password
                row[activated] = user.activated
                row[role] = user.role
            }
        }
        return findByEmail(user.email)
    }

    fun activate(id: Long) {
        transaction {
            Users.update({ Users.id eq id }) { row ->
                row[activated] = true
            }
        }
    }

    fun findAll(): List<User> {
        return transaction {
            Users.selectAll().map { Users.toDomain(it) }.filter { it.role != Role.ADMIN }
        }
    }

    private fun createDefaultAdminUser() {
        if (findByEmail(adminEmail) == null) {
            val randomPassword = generateRandomPassword(20)
            create(
                User(
                    id = 1,
                    email = "",
                    username = "Christopher Werner",
                    password = String(Base64.getEncoder().encode(Cipher.encrypt(randomPassword))),
                    role = Role.ADMIN,
                    activated = true
                )
            )

            val file = File("admin.txt")
            file.writeText("$adminEmail\n$randomPassword")
            println("Admin password: $randomPassword")
        }
    }
}
