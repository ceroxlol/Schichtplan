package schichtplanhgl.validation

import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.direct
import org.kodein.di.instance
import schichtplanhgl.validation.result.ValidationResult
import schichtplanhgl.validation.validators.Validator

class InputValidator(
    override val di: DI
) : DIAware {

    @Suppress("UNCHECKED_CAST")
    inline fun <reified T> validate(param: T) =
        try {
            (di.direct.instance<Validator<*>>(tag = T::class.java) as? Validator<T>)?.validate(param)
                ?: ValidationResult.GenericError
        } catch (e: DI.NotFoundException) {
            /*Log.d {
                "No available validator binding found, ${e.message}"
            }*/
            ValidationResult.GenericError
        }

    inline fun <reified T : Any> T.isValid() =
        validate(this@isValid).let { result ->
            if (result is ValidationResult.Result) {
                result.results.isEmpty()
            } else {
                result == ValidationResult.Valid
            }
        }
}