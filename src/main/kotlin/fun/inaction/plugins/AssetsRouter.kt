package `fun`.inaction.plugins

import `fun`.inaction.utils.config
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.routing.*

fun Application.assetsRoute(){
    routing {
        static("static"){
            files("${config.rootAssetsPath}/static")
        }

        static ("img"){
            files("${config.rootAssetsPath}/img")
        }

        static("front"){
            files("${config.rootAssetsPath}/front")
        }
    }
}