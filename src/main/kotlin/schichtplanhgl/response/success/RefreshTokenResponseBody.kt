package schichtplanhgl.response.success

import schichtplanhgl.response.ResponseBody

data class RefreshTokenResponseBody(val token: String, val refreshToken: String) : ResponseBody