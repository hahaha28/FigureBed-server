package `fun`.inaction.utils

import com.tinify.ClientException
import com.tinify.Tinify
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.concurrent.locks.ReentrantLock


object ImageUtil {
    private val tokenList = config.tokenList

    val BASE_PATH by lazy { "${config.rootAssetsPath}/img" }

    private var index: Int? = null

    private val indexLock = ReentrantLock()

    private val okHttpClient = OkHttpClient()

    init {
        Tinify.setKey(config.tinifyKey)
    }

    fun verifyToken(token: String): Boolean {
        return token in tokenList
    }

    /**
     * 保存网络图片
     * @return 返回图片文件名（不是路径）
     */
    fun saveImage(url: String) :String{
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        val response = okHttpClient.newCall(request).execute()
        val inputStream = response.body!!.byteStream()
        val type = response.header("content-type","unknow")!!.substringAfter("/")
        val fileName = "${getIndex()}.${type}"
        inputStream.toFile("${BASE_PATH}/${fileName}")
        return fileName
    }

    /**
     * 保存图片
     * @return 返回图片文件名（不是路径）
     */
    fun saveImage(inputStream:InputStream,type:String):String{
        val fileName = "${getIndex()}.${type}"
        inputStream.toFile("${BASE_PATH}/${fileName}")
        return fileName
    }

    /**
     * 压缩图片
     */
    fun compressPicture(from: String, to: String) {
        if (!from.endsWith("jpg")
            && !from.endsWith("jpeg")
            && !from.endsWith("png")) {
            return
        }
        try {
            Tinify.fromFile(from).toFile(to)
        } catch (e: ClientException) {
            println("压缩失败！格式不支持!${e.toString()}")
        }
    }

    /**
     * 压缩并替换图片
     */
    fun compressPicture(path:String) = compressPicture(path,path)

    /**
     * 获取照片索引
     */
    private fun getIndex(): Int {
        indexLock.lock()
        if (index == null) {
            index = File(BASE_PATH).listFiles()!!.maxOfOrNull { it ->
                it.name.substringBefore(".").toInt()
            }
            if (index == null) {
                index = 999
            }
        }
        index = index!! + 1
        indexLock.unlock()
        return index!!
    }

}