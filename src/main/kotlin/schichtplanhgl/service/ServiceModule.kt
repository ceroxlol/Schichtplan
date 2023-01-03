package schichtplanhgl.service

import org.kodein.di.*
import schichtplanhgl.service.token.JWTTokenVerifierService
import schichtplanhgl.service.token.JWTTokenVerifierServiceImpl
import schichtplanhgl.service.token.JwtTokenService
import schichtplanhgl.service.token.TokenService
import schichtplanhgl.service.user.UserService
import schichtplanhgl.service.user.UserServiceImpl
import schichtplanhgl.util.parameters.ServiceParameters
import schichtplanhgl.validation.InputValidator

fun DI.MainBuilder.bindServices(serviceParameters: ServiceParameters) {

    bindSingleton<UserService>{ UserServiceImpl(instance()) }

    bindSingleton<TokenService>{
        JwtTokenService(
            serviceParameters.jwtParameters.algorithm,
            serviceParameters.jwtParameters.validityPeriod,
            serviceParameters.jwtParameters.issuer,
            instance()
        )
    }

    bindSingleton<JWTTokenVerifierService>{
        JWTTokenVerifierServiceImpl(
            serviceParameters.jwtParameters.algorithm,
            serviceParameters.jwtParameters.issuer
        )
    }

    bindSingleton { InputValidator(di) }
}