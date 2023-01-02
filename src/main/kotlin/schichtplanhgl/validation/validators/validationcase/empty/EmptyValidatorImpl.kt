package schichtplanhgl.validation.validators.validationcase.empty

import schichtplanhgl.validation.result.ValidationResult

class EmptyValidatorImpl : EmptyValidator {

    override fun validate(field: String) =
        if (field.isEmpty()) {
            ValidationResult.Empty
        } else {
            ValidationResult.Valid
        }
}