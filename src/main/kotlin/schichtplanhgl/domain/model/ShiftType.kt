package schichtplanhgl.domain.model

enum class ShiftType {
    DEFAULT,
    VACATION,
    SICK
}

enum class ShiftTypeDto {
    Normal,
    Urlaub,
    Krankheit
}

fun ShiftType.toDto() =
    when(this){
        ShiftType.DEFAULT -> ShiftTypeDto.Normal
        ShiftType.SICK -> ShiftTypeDto.Krankheit
        ShiftType.VACATION -> ShiftTypeDto.Urlaub
    }

fun ShiftTypeDto.toShiftType() =
    when(this){
        ShiftTypeDto.Normal -> ShiftType.DEFAULT
        ShiftTypeDto.Krankheit -> ShiftType.SICK
        ShiftTypeDto.Urlaub -> ShiftType.VACATION
    }