package schichtplanhgl.web

import kotlinx.serialization.Serializable

@Serializable
internal data class ErrorResponse(val errors: Map<String, List<String?>>)

object ErrorExceptionMapping {
}
