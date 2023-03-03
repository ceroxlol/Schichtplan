package schichtplanhgl.domain.repository

import kotlinx.datetime.Instant
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import schichtplanhgl.domain.Shift
import schichtplanhgl.domain.ShiftWithUserName

internal object Shifts : LongIdTable() {
    val userId: Column<Long> = long("userId").references(Users.id)
    val start = timestamp("shiftStart")
    val end = timestamp("shiftEnd")

    fun toDomain(row: ResultRow): Shift {
        return Shift(
            id = row[id].value,
            start = row[start],
            end = row[end],
            userId = row[userId],
        )
    }

    fun toShiftWithUserName(row: ResultRow): ShiftWithUserName{
        return ShiftWithUserName(
            id = row[id].value,
            start = row[start],
            end = row[end],
            userId = row[userId],
            userName = row[Users.username]
        )
    }
}

class ShiftRepository {
    init {
        transaction {
            SchemaUtils.create(Shifts)
            //TODO Debugging
            Shifts.deleteAll()
        }
        val start = Instant.fromEpochMilliseconds(1677754036000)
        val end = Instant.fromEpochMilliseconds(1677775636000)
        createShift(Shift(
            id = 1,
            userId = 1,
            start = start,
            end = end
        ))
    }

    fun getAll(): List<ShiftWithUserName>{
        return transaction {
            (Shifts innerJoin Users).selectAll().map { Shifts.toShiftWithUserName(it) }
        }
    }

    fun findByUserId(userId: Long): List<Shift>{
        return transaction {
            Shifts.select { Shifts.userId eq userId }
                .map { Shifts.toDomain(it) }
        }
    }

    fun createShift(shift: Shift): Long {
        return transaction {
            Shifts.insertAndGetId { row ->
                row[userId] = shift.userId
                row[start] = shift.start
                row[end] = shift.end
            }.value
        }
    }
}
