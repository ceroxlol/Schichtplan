package schichtplanhgl.domain

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.Serializable

@Serializable
class ShiftDto(
    val id: Long,
    val employeeId: Long,    //userId
    val title: String,
    val start: Instant,
    val end: Instant
)

open class Shift(
    val id: Long,
    val userId: Long,
    val start: Instant,
    val end: Instant
)

class ShiftWithUserName(
    id: Long,
    userId: Long,
    start: Instant,
    end: Instant,
    val userName: String
) : Shift(id, userId, start, end)

fun ShiftWithUserName.toDto() = ShiftDto(
    id = id,
    employeeId = userId,
    title = createTitle(),
    start = start,
    end = end
)

fun Shift.toDto() = ShiftDto(
    id = id,
    employeeId = userId,
    title = createTitle(),
    start = start,
    end = end
)

private fun Shift.createTitle() =
    start.toLocalDateTime(TimeZone.UTC).hour.toString() + " - " + end.toLocalDateTime(TimeZone.UTC).hour.toString()