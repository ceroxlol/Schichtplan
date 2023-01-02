package schichtplanhgl.validation

import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import schichtplanhgl.model.Login
import schichtplanhgl.model.Password
import schichtplanhgl.validation.validators.Validator
import schichtplanhgl.validation.validators.input.login.LoginValidator
import schichtplanhgl.validation.validators.input.password.PasswordValidator
import schichtplanhgl.validation.validators.validationcase.blank.BlankValidatorImpl
import schichtplanhgl.validation.validators.validationcase.empty.EmptyValidatorImpl

fun DI.MainBuilder.bindValidators() {

    bind<Validator<*>>(tag = Password::class.java) with singleton {
        PasswordValidator(
            BlankValidatorImpl(),
            EmptyValidatorImpl()
        )
    }

    bind<Validator<*>>(tag = Login::class.java) with singleton {
        LoginValidator(
            EmptyValidatorImpl(),
            BlankValidatorImpl()
        )
    }
//TODO
/*    bind<Validator<*>>(tag = Name::class.java) with singleton {
        NameValidator(
            BlankValidatorImpl(),
            EmptyValidatorImpl()
        )
    }*/
}