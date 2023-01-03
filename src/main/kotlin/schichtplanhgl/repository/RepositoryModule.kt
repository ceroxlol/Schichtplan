package schichtplanhgl.repository

import org.kodein.di.*
import schichtplanhgl.repository.hash.HashRepository
import schichtplanhgl.repository.hash.SaltingHashRepository
import schichtplanhgl.util.parameters.SALT

fun DI.MainBuilder.bindRepositories() {

    bindSingleton<HashRepository>{ SaltingHashRepository(instance(tag = SALT)) }
}