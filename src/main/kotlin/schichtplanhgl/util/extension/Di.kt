package schichtplanhgl.util.extension

import io.ktor.server.application.*
import io.ktor.server.routing.*
import org.kodein.di.DI
import org.kodein.di.direct
import org.kodein.di.instance

inline fun <reified T : Any> Routing.instance(di: DI, tag: Any? = null) = di.direct.instance<T>(tag)

inline fun <reified T : Any> Application.instance(di: DI, tag: Any? = null) = di.instance<T>(tag)