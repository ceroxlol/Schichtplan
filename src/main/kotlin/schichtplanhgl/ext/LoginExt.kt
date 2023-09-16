package schichtplanhgl.ext

import schichtplanhgl.domain.model.Login
import schichtplanhgl.domain.model.Role

fun Login.isAdminOrHeimleitung() = role == Role.ADMIN || role == Role.HEIMLEITUNG