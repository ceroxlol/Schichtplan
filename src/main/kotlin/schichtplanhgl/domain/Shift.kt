package schichtplanhgl.domain

import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import kotlin.time.Duration

@Serializable
class ShiftDto(
    val userId : Long,
    val start: Instant,
    val end: Instant,
    val duration: Duration
)

class Shift(
    val userId: Long,
    val start: Instant,
    val end: Instant
)

fun Shift.toDto() = ShiftDto(
    userId = userId,
    start = start,
    end = end,
    duration = end - start
)

fun ShiftDto.toShift() = Shift(
    userId = userId,
    start = start,
    end = end
)