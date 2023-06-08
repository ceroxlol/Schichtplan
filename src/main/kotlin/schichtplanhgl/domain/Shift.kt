package schichtplanhgl.domain

import kotlinx.datetime.*
import kotlinx.serialization.Serializable

@Serializable
class ShiftDto(
    val id: Long?,
    val employeeId: Long,    //userId
    val start: Instant,
    val end: Instant,
    val type: ShiftTypeDto
)

class Shift(
    val id: Long,
    val userId: Long,
    val start: Instant,
    val end: Instant,
    val type: ShiftType
)

fun Shift.toDto() = ShiftDto(
    id = id,
    employeeId = userId,
    start = start,
    end = end,
    type = type.toDto()
)

fun ShiftDto.toShift() = Shift(
    id = id ?: -1,
    userId = employeeId,
    start = start,
    end = end,
    type = type.toShiftType()
)

private fun Shift.createTitle() = start.convertToHoursMinutes() + " - " + end.convertToHoursMinutes()

fun Instant.convertToHoursMinutes(): String {
    val localDateTime = this.toLocalDateTime(TimeZone.currentSystemDefault())
    return localDateTime.hour.toString() + ":" + localDateTime.minute
}