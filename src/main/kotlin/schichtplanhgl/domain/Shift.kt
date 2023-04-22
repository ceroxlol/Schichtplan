package schichtplanhgl.domain

import kotlinx.datetime.*
import kotlinx.serialization.Serializable

@Serializable
class ShiftDto(
    val id: Long?,
    val employeeId: Long,    //userId
    val title: String,
    val start: Instant,
    val end: Instant
)

open class Shift(
    val id: Long?,
    val employeeId: Long,
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
    employeeId = employeeId,
    title = createTitle(),
    start = start,
    end = end
)

fun Shift.toDto() = ShiftDto(
    id = id,
    employeeId = employeeId,
    title = createTitle(),
    start = start,
    end = end
)

fun ShiftDto.toShift() = Shift(
    id = this.id,
    employeeId = employeeId,
    start = start,
    end = end
)

private fun Shift.createTitle() = start.convertToHoursMinutes() + " - " + end.convertToHoursMinutes()

fun Instant.convertToHoursMinutes(): String {
    val localDateTime = this.toLocalDateTime(TimeZone.currentSystemDefault())
    return localDateTime.hour.toString() + ":" + localDateTime.minute
}