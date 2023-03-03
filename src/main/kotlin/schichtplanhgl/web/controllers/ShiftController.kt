package schichtplanhgl.web.controllers

import io.ktor.server.application.*
import io.ktor.server.response.*
import schichtplanhgl.domain.service.ShiftService
import schichtplanhgl.domain.toDto

class ShiftController(
    private val shiftService: ShiftService
) {
    suspend fun getShiftsByUserId(userId: Long, ctx: ApplicationCall) {
        shiftService.getShiftsByUserId(userId)
            .also {
                ctx.respond(it.map { it.toDto() })
            }
    }

    suspend fun getAll(ctx: ApplicationCall) {
        ctx.respond(shiftService.getAll().map { it.toDto().also { ctx.application.log.info(it.start.toString()) } })
    }
}