package `fun`.inaction

import `fun`.inaction.utils.ImageUtil
import com.tinify.Tinify
import okhttp3.OkHttpClient
import java.util.*


fun main() {
//    println(ImageUtil.getIndex())


    val startTime = System.currentTimeMillis()
    Tinify.setKey("z9gM122Z6dYtSHZ6HNn4rx34C6kVYrSH")
    Tinify.fromFile("E:/Network.svg").toFile("E:/test.svg")
    val endTime = System.currentTimeMillis()
    println("${(endTime-startTime)}ms")
}