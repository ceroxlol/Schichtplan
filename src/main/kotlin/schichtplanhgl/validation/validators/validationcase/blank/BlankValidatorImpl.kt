package schichtplanhgl.validation.validators.validationcase.blank

import schichtplanhgl.validation.result.ValidationResult

class BlankValidatorImpl : BlankValidator {

    override fun validate(field: String) =
        if (field.isBlank()) {
            ValidationResult.Blank
        } else {
            ValidationResult.Valid
        }
}