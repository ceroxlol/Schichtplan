package schichtplanhgl.domain

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class ShiftDto(
    val id: Long,
    val group : Long,
    val title: String,
    @SerialName("start_time") val start: Long,
    @SerialName("end_time") val end: Long
)

class Shift(
    val id: Long,
    val userId: Long,
    val start: Instant,
    val end: Instant
)

class ShiftWithUserName(
    val id: Long,
    val userId: Long,
    val userName: String,
    val start: Instant,
    val end: Instant
)

fun ShiftWithUserName.toDto() = ShiftDto(
    id = id,
    group = 1,
    title = start.toLocalDateTime(TimeZone.UTC).hour.toString() + " - " + end.toLocalDateTime(TimeZone.UTC).hour.toString(),
    start = start.toEpochMilliseconds(),
    end = end.toEpochMilliseconds()
)