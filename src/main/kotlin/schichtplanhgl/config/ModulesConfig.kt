package schichtplanhgl.config

import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import schichtplanhgl.domain.repository.ShiftRepository
import schichtplanhgl.domain.repository.UserRepository
import schichtplanhgl.domain.service.ShiftService
import schichtplanhgl.domain.service.UserService
import schichtplanhgl.web.controllers.ShiftController
import schichtplanhgl.web.controllers.UserController

object ModulesConfig {
    private val userModule = DI.Module("USER") {
        bindSingleton { UserController(instance()) }
        bindSingleton { UserService(instance()) }
        bindSingleton { UserRepository() }
    }
    private val shiftModule = DI.Module("SHIFT") {
        bindSingleton { ShiftController(instance()) }
        bindSingleton { ShiftService(instance()) }
        bindSingleton { ShiftRepository() }
    }
    internal val di = DI {
        import(userModule)
        import(shiftModule)
    }
}
