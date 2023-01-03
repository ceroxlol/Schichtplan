package schichtplanhgl.util.extension

import com.auth0.jwt.interfaces.Payload
import io.ktor.server.auth.jwt.*
import schichtplanhgl.model.Login
import schichtplanhgl.service.token.JwtTokenService

val JWTCredential.loginFromPayload
    get() = Login(payload.getClaim(JwtTokenService.LOGIN_CLAIM_NAME).asString())

val Payload.userLogin
    get() = Login(getClaim(JwtTokenService.LOGIN_CLAIM_NAME).asString())