package schichtplanhgl.ext

import schichtplanhgl.domain.model.User

fun User.verifyId(id: Long) = this.id == id