package schichtplanhgl.service.user

import schichtplanhgl.model.Login

interface UserExistenceService {

    fun doesUserExist(login: Login): Boolean
}