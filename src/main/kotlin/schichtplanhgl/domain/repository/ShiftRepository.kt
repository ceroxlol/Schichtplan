package schichtplanhgl.domain.repository

import kotlinx.datetime.*
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp
import org.jetbrains.exposed.sql.transactions.transaction
import schichtplanhgl.domain.Shift

internal object Shifts : LongIdTable() {
    val userId: Column<Long> = long("userId")/*.references(Users.id)*/
    val start = timestamp("shiftStart")
    val end = timestamp("shiftEnd")

    fun toDomain(row: ResultRow): Shift {
        return Shift(
            userId = row[userId],
            start = row[start],
            end = row[end]
        )
    }
}

class ShiftRepository {
    init {
        transaction {
            SchemaUtils.create(Shifts)
        }
        val start = Instant.parse("2023-02-28T06:39:17.300718573Z")
        val end = Instant.parse("2023-02-28T11:39:17.300718573Z")
        createShift(Shift(
            1,
            start,
            end
        ))
    }

    fun getAll(): List<Shift>{
        return transaction {
            Shifts.selectAll().map { Shifts.toDomain(it) }
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
