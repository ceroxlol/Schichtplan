package schichtplanhgl.response.success

import schichtplanhgl.response.ResponseBody

data class LoginResponseBody(
    val token: String,
    val refreshToken: String
) : ResponseBody