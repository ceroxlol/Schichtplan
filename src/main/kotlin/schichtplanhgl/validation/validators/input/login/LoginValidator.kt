package schichtplanhgl.validation.validators.input.login

import schichtplanhgl.model.Login
import schichtplanhgl.validation.validators.Validator
import schichtplanhgl.validation.validators.input.BaseValidator
import schichtplanhgl.validation.validators.validationcase.blank.BlankValidator
import schichtplanhgl.validation.validators.validationcase.empty.EmptyValidator

class LoginValidator(
    private val emptyValidator: EmptyValidator,
    private val blankValidator: BlankValidator
) : BaseValidator, Validator<Login> {

    override fun validate(field: Login) =
        validate(
            emptyValidator.validate(field.value),
            blankValidator.validate(field.value)
        )
}