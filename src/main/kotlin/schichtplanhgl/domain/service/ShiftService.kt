package schichtplanhgl.domain.service

import schichtplanhgl.domain.Shift
import schichtplanhgl.domain.repository.ShiftRepository

class ShiftService(
    private val shiftRepository: ShiftRepository
) {

    fun getShiftsByUserId(userId: Long) = shiftRepository.findByUserId(userId)

    fun getAll() = shiftRepository.getAll()

    fun createShift(shift: Shift) = shiftRepository.createShift(shift)

    fun updateShift(shift:Shift) = shiftRepository.updateShift(shift)

    fun deleteShift(id: Long) = shiftRepository.deleteShift(id)
}