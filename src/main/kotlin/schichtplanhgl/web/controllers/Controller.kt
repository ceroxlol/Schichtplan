package schichtplanhgl.web.controllers

import schichtplanhgl.domain.model.Login
import schichtplanhgl.ext.isAdminOrHeimleitung

abstract class Controller {

    protected fun selfOrAdmin(userId: Long, login: Login) : Boolean {
        return login.isAdminOrHeimleitung() || login.id == userId
    }
}