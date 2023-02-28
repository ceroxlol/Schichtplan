package schichtplanhgl.domain

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class ShiftPlanDto(
    val days : List<LocalDateTime>

) {
}

class ShiftPlan {
}