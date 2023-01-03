package schichtplanhgl.util.parameters

import com.auth0.jwt.algorithms.Algorithm
import schichtplanhgl.model.Milliseconds

data class JwtParameters(
    val algorithm: Algorithm,
    val validityPeriod: Milliseconds,
    val issuer: String
)