package schichtplanhgl.web.controllers

import io.ktor.server.application.*
import io.ktor.server.response.*
import schichtplanhgl.domain.service.ShiftService

class ShiftController(
    private val shiftService: ShiftService
) {
    suspend fun getShiftsByUserId(userId: Long, ctx: ApplicationCall) {
        shiftService.getShiftsByUserId(userId)
            .also {
                ctx.respond(it)
            }
    }

    suspend fun getAll(ctx: ApplicationCall) {
        ctx.respond(shiftService.getAll())
    }
}