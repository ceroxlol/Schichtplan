package schichtplanhgl.domain.repository

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import schichtplanhgl.domain.Role
import schichtplanhgl.domain.User

internal object Users : LongIdTable() {
    val email: Column<String> = varchar("email", 200).uniqueIndex()
    val username: Column<String> = varchar("username", 100)
    val password: Column<String> = varchar("password", 150)
    val role: Column<Role> = enumerationByName("role", 50, Role::class).default(Role.MITARBEITER)
    val activated: Column<Boolean> = bool("activated")

    fun toDomain(row: ResultRow): User {
        return User(
            id = row[Users.id].value,
            email = row[email],
            username = row[username],
            password = row[password],
            role = row[role],
            activated = row[activated]
        )
    }
}

class UserRepository {
    init {
        transaction {
            SchemaUtils.create(Users)
        }
    }

    fun findByEmail(email: String): User? {
        return transaction {
            Users.select { Users.email eq email }
                .map { Users.toDomain(it) }
                .firstOrNull()
        }
    }

    fun findByUsername(username: String): User? {
        return transaction {
            Users.select { Users.username eq username }
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

    fun update(email: String, user: User): User? {
        transaction {
            Users.update({ Users.email eq email }) { row ->
                row[Users.email] = user.email
                row[username] = user.username
                row[password] = user.password
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
            Users.selectAll().map { Users.toDomain(it) }
        }
    }
}
