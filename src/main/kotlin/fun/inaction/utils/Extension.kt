package `fun`.inaction.utils

import java.io.*

fun InputStream.toFile(path:String){
    val bis = BufferedInputStream(this)

    val file = File(path)
    if(!file.exists()){
        file.createNewFile()
    }
    val fos = FileOutputStream(file)
    val bos = BufferedOutputStream(fos)

    val bytes = ByteArray(1024*20)
    var len:Int
    while( (bis.read(bytes).also { len = it }) != -1){
        bos.write(bytes,0,len)
    }
    bos.close()
}