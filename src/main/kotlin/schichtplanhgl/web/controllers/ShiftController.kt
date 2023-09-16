package schichtplanhgl.web.controllers

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import schichtplanhgl.domain.model.ShiftDto
import schichtplanhgl.domain.service.ShiftService
import schichtplanhgl.domain.model.toDto
import schichtplanhgl.domain.model.toShift

class ShiftController(
    private val shiftService: ShiftService
) {
    private val logger: Logger = LoggerFactory.getLogger("ShiftController")
    suspend fun getShiftsByUserId(userId: Long, ctx: ApplicationCall) {
        shiftService.getShiftsByUserId(userId)
            .also {
                it.map { logger.debug(it.toString()) }
                ctx.respond(it.map { it.toDto() })
            }
    }

    suspend fun getAll(ctx: ApplicationCall) {
        ctx.respond(shiftService.getAll().map { it.toDto() })
    }

    suspend fun upsertShift(ctx: ApplicationCall) {
        ctx.receive<ShiftDto>().toShift().apply {
            ctx.respond(
                if (this.id == (-1).toLong()) {
                    shiftService.createShift(this)
                } else {
                    shiftService.updateShift(this)
                }
            )
        }
    }

    suspend fun deleteShift(shiftId: Long, ctx: ApplicationCall) {
        shiftService.deleteShift(shiftId)
        ctx.respond(HttpStatusCode.OK)
    }
}