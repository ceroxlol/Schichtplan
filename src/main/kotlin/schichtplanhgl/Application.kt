package schichtplanhgl

import com.auth0.jwt.JWTVerifier
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.engine.*
import io.ktor.server.locations.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.direct
import org.kodein.di.instance
import schichtplanhgl.controller.bindControllers
import schichtplanhgl.db.H2Db
import schichtplanhgl.interactor.bindInteractors
import schichtplanhgl.repository.bindRepositories
import schichtplanhgl.routing.login
import schichtplanhgl.routing.register
import schichtplanhgl.service.bindServices
import schichtplanhgl.service.token.JWTTokenVerifierService
import schichtplanhgl.service.user.UserService
import schichtplanhgl.util.extension.*
import schichtplanhgl.util.parameters.JwtParameters
import schichtplanhgl.util.parameters.ServiceParameters
import schichtplanhgl.util.parameters.bindParameters
import schichtplanhgl.validation.bindValidators

const val port = 8080
const val host = "0.0.0.0"
val module = Application::module

private val Application.userService
    get() = instance<UserService>()

private val Application.jwtTokenVerifierService
    get() = instance<JWTTokenVerifierService>()

fun main() {
    embeddedServer(
        Netty,
        port = port,
        host = host,
        module = module
    ).also {
        print("Started. Listening on $port...")
    }
        .start(wait = true)
}

fun Application.main() {
    val di = bindDI()
    installFeatures()
    setupDb()
    bindRouting(di)
}

private fun Application.installFeatures() {
    install(ContentNegotiation) {
        json()
    }
    install(CallLogging)

    install(Authentication) {
        jwt {
            realm = this@installFeatures.environment.config.realm
            verifier(this@installFeatures.buildJwtVerifier())
            validate { this@installFeatures.validateCredential(it) }
        }
    }
}

private fun setupDb() {
    H2Db.init()
}

private fun Application.validateCredential(jwtCredential: JWTCredential) =
    if (userService.doesUserExist(jwtCredential.loginFromPayload)) {
        JWTPrincipal(jwtCredential.payload)
    } else {
        null
    }

private fun Application.bindDI(): DI {
    return DI {
        bindParameters(
            salt = environment.config.stringProperty("config.SALT")
        )
        bindValidators()
        bindControllers()
        bindInteractors()
        bindServices(obtainParameters())
        bindRepositories()
    }
}

@OptIn(KtorExperimentalLocationsAPI::class)
private fun Application.bindRouting(di: DI) {
    routing {
        login(instance(di))
        register(instance(di))
    }
}

private fun Application.obtainParameters() = environment.config.run {
    ServiceParameters(
        jwtParameters = JwtParameters(
            algorithm,
            validity,
            issuer
        )
    )
}

private fun Application.buildJwtVerifier() : JWTVerifier = environment.config.run {
    jwtTokenVerifierService.buildVerifier(algorithm, issuer)
}