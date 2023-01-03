package schichtplanhgl.interactor

import org.kodein.di.*
import schichtplanhgl.interactor.login.AuthInteractor
import schichtplanhgl.interactor.login.AuthInteractorImpl
import schichtplanhgl.interactor.register.RegisterInteractor
import schichtplanhgl.interactor.register.RegisterInteractorImpl

fun DI.MainBuilder.bindInteractors() {

    bindSingleton<AuthInteractor>{ AuthInteractorImpl(instance(), instance()) }

    bindSingleton<RegisterInteractor>{ RegisterInteractorImpl(instance()) }
}