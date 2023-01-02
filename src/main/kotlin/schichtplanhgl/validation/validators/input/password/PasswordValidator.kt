package schichtplanhgl.validation.validators.input.password

import schichtplanhgl.model.Password
import schichtplanhgl.validation.validators.Validator
import schichtplanhgl.validation.validators.input.BaseValidator
import schichtplanhgl.validation.validators.validationcase.blank.BlankValidator
import schichtplanhgl.validation.validators.validationcase.empty.EmptyValidator

class PasswordValidator(
    private val blankValidator: BlankValidator,
    private val emptyValidator: EmptyValidator
) : BaseValidator, Validator<Password> {

    override fun validate(field: Password) =
        validate(
            blankValidator.validate(field.value),
            emptyValidator.validate(field.value)
        )
}