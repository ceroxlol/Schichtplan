package schichtplanhgl.domain

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ShiftDto(
    val id: Long,
    val group: Long,
    val title: String,
    @SerialName("start_time") val start: Long,
    @SerialName("end_time") val end: Long
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
    group = userId,
    title = createTitle(),
    start = start.toEpochMilliseconds(),
    end = end.toEpochMilliseconds()
)

fun Shift.toDto() = ShiftDto(
    id = id,
    group = userId,
    title = createTitle(),
    start = start.toEpochMilliseconds(),
    end = end.toEpochMilliseconds()
)

private fun Shift.createTitle() =
    start.toLocalDateTime(TimeZone.UTC).hour.toString() + " - " + end.toLocalDateTime(TimeZone.UTC).hour.toString()