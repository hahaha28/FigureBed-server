package `fun`.inaction

import `fun`.inaction.plugins.assetsRoute
import `fun`.inaction.plugins.configureRouting
import `fun`.inaction.utils.config
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args:Array<String>) {
    embeddedServer(Netty, port = config.port, host = config.host) {
        install(CallLogging)
        configureRouting()
        assetsRoute()

    }.start(wait = true)
}
