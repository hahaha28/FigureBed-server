package `fun`.inaction.plugins

import `fun`.inaction.utils.ImageUtil
import `fun`.inaction.utils.config
import `fun`.inaction.utils.toFile
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.response.*
import io.ktor.request.*
import java.io.File
import java.io.InputStream

fun Application.configureRouting() {

    routing {
        get("/") {
            call.respondRedirect("/front/home.html")
        }

        get("/{fileName}") {
            call.respondRedirect("/img/${call.parameters["fileName"]}")
        }

        // 上传图片文件
        post("/upload/raw") {
            var token: String = ""
            var type: String = "unknow"
            val compress = call.request.queryParameters["compress"]?.toBoolean() ?: false
            val multipart = call.receiveMultipart()
            var filePart:PartData.FileItem? = null
            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        when (part.name) {
                            "token" -> token = part.value
                            "type" -> type = part.value
                        }
                    }
                    is PartData.FileItem -> {
                        filePart = part
                    }
                }
            }

            // 首先验证 token
            if (!ImageUtil.verifyToken(token)) {
                call.respondText { "你没有权限使用这个图床" }
            } else {
                // 下载图片到本地
                filePart!!.contentType?.let{
                    type = it.contentSubtype
                }
                println("type:${type}")
                val fileName = ImageUtil.saveImage(filePart!!.streamProvider(), type)
                // 判断是否要压缩
                if (compress) {
                    ImageUtil.compressPicture("${ImageUtil.BASE_PATH}/${fileName}")
                }
                call.respondText { "${config.baseUrl}/${fileName}" }

            }

        }

        // 上传url
        post("/upload/url") {
            val forms = call.receiveParameters()
            val token = forms["token"]!!
            val url = forms["url"]!!
            val compress = call.request.queryParameters["compress"]?.toBoolean() ?: false
            // 首先验证 token
            if (!ImageUtil.verifyToken(token)) {
                call.respondText { "你没有权限使用这个图床" }
            } else {
                // 下载图片到本地
                val fileName = ImageUtil.saveImage(url)
                // 判断是否要压缩
                if (compress) {
                    ImageUtil.compressPicture("${ImageUtil.BASE_PATH}/${fileName}")
                }
                call.respondText { "${config.baseUrl}/${fileName}" }
            }
        }

        // 删除图片
        post("/delete") {
            val forms = call.receiveParameters()
            val url = forms["url"]!!
            val file: File
            if (url.startsWith("static")) {
                // 如果是老版的图床
                file = File("${config.rootAssetsPath}/${url}")
            } else {
                // 新版的图床
                file = File("${config.rootAssetsPath}/img/${url}")
            }
            // 移入回收站，删除原文件
            if (file.exists()) {
                val newFileName = url.replace('/', '_')
                file.copyTo(File("${config.rootAssetsPath}/recycle/${newFileName}"))
                file.delete()
                call.respondText { "ok" }
            } else {
                call.respondText { "找不到文件，${file.absolutePath}" }
            }
        }

        // 删除图片的前端
        get("/delete") {
            call.respondRedirect("/front/delete.html")
        }

    }

}
