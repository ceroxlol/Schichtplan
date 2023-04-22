package schichtplanhgl.domain.repository

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.plus
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
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
            employeeId = row[userId],
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
        val start = Clock.System.now()
        val end = Clock.System.now().plus(8, DateTimeUnit.HOUR)
        createShift(Shift(
            id = 1,
            employeeId = 1,
            start = start,
            end = end
        ))
        createShift(Shift(
            id = 2,
            employeeId = 2,
            start = start,
            end = end
        ))
        createShift(Shift(
            id = 1,
            employeeId = 1,
            start = start.plus(24, DateTimeUnit.HOUR),
            end = end.plus(24, DateTimeUnit.HOUR)
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
                row[userId] = shift.employeeId
                row[start] = shift.start
                row[end] = shift.end
            }.value
        }
    }

    fun updateShift(shift: Shift) {
        return transaction{
            Shifts.update ({ Shifts.id eq shift.id } ){ row ->
                row[userId] = shift.employeeId
                row[start] = shift.start
                row[end] = shift.end
            }
        }
    }

    fun deleteShift(id: Long){
        transaction {
            Shifts.deleteWhere { Shifts.id eq id }
        }
    }
}
