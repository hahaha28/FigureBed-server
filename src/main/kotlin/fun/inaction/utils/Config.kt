package `fun`.inaction.utils

import com.google.gson.Gson
import com.google.gson.JsonElement
import java.io.File

data class Config(
    val baseUrl: String,
    val rootAssetsPath: String,
    val port: Int,
    val host: String,
    val tokenList :List<String>,
    val tinifyKey:String
)