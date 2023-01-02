package schichtplanhgl.validation.validators

import schichtplanhgl.validation.result.ValidationResult

interface Validator<T> {

    fun validate(field: T): ValidationResult
}