package schichtplanhgl.controller

import org.kodein.di.*
import schichtplanhgl.controller.login.LoginController
import schichtplanhgl.controller.login.LoginControllerImpl
import schichtplanhgl.controller.register.RegisterController
import schichtplanhgl.controller.register.RegisterControllerImpl

fun DI.MainBuilder.bindControllers() {

    bindSingleton<LoginController> { LoginControllerImpl(instance(), instance()) }

    bindSingleton<RegisterController> { RegisterControllerImpl(instance(), instance()) }

}