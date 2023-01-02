package schichtplanhgl.db.tables.user

import schichtplanhgl.model.Login
import schichtplanhgl.model.Name
import schichtplanhgl.model.Password

data class UserTile(
    val login: Login,
    val password: Password,
    val name: Name
)