package schichtplanhgl.service.token

import com.auth0.jwt.JWTVerifier

interface JWTTokenVerifierService {

    fun obtainVerifier(): JWTVerifier
}