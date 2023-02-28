package schichtplanhgl.domain.service

import schichtplanhgl.domain.repository.ShiftRepository

class ShiftService(
    private val shiftRepository: ShiftRepository
) {

    fun getShiftsByUserId(userId: Long) = shiftRepository.findByUserId(userId)

    fun getAll() = shiftRepository.getAll()
}