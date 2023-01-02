package schichtplanhgl.validation.validators.input

import schichtplanhgl.validation.result.ValidationResult

interface BaseValidator {

    fun validate(vararg validationResult: ValidationResult) =
        ValidationResult.Result().apply {
            addResults(validationResult.filter { it != ValidationResult.Valid })
        }
}