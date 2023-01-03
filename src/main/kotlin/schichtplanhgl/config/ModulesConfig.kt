package schichtplanhgl.config

import schichtplanhgl.domain.service.UserService
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import schichtplanhgl.domain.repository.UserRepository
import schichtplanhgl.utils.JwtProvider
import schichtplanhgl.web.controllers.UserController

object ModulesConfig {
    private val userModule = DI.Module("USER") {
        bindSingleton { UserController(instance()) }
        bindSingleton { UserService(JwtProvider, instance()) }
        bindSingleton { UserRepository() }
    }
    internal val di = DI {
        import(userModule)
    }
}
