package schichtplanhgl.interactor

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton
import schichtplanhgl.interactor.login.AuthInteractor
import schichtplanhgl.interactor.login.AuthInteractorImpl

fun DI.MainBuilder.bindInteractors() {
    bind<AuthInteractor>() with singleton { AuthInteractorImpl(instance(), instance()) }
}