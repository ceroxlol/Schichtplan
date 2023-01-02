package schichtplanhgl.db.tables.user

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import schichtplanhgl.model.Login
import schichtplanhgl.model.Name
import schichtplanhgl.model.Password

class User(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<User>(Users)

    var login by Users.login
    var password by Users.password
    var name by Users.name
}

fun User.toUserTile() = UserTile(
    Login(login),
    Password(password),
    Name(name)
)