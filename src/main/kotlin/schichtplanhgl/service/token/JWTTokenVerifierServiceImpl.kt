package schichtplanhgl.service.token

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm

class JWTTokenVerifierServiceImpl(algorithm: Algorithm, issuer: String) : JWTTokenVerifierService {

    private val verifier = lazy {
        JWT.require(algorithm)
            .withIssuer(issuer)
            .build()
    }

    override fun obtainVerifier(): com.auth0.jwt.JWTVerifier = verifier.value
}