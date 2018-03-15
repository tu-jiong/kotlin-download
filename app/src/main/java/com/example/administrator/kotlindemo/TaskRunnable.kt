package com.example.administrator.kotlindemo

import android.util.Log
import okhttp3.Request
import okio.Okio
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.URL

/**
 * Created by Tujiong on 2017/5/24.
 */
class TaskRunnable(val taskEntity: TaskEntity, val listener: TaskListener) : Runnable {

    override fun run() {

//        makeException()
        taskEntity.status = STATUS_START
        listener.onStart(taskEntity)
        val file = getCacheFile(taskEntity)
        val okHttpClient = NetManager.getOkHttpClient()
        val request = Request.Builder()
                .url(taskEntity.url)
                .build()
        val response = okHttpClient.newCall(request).execute()
        val code = response.code()
        if (code == 200 || code == 206) {
            val contentLength = response.body()?.contentLength() ?: 0
            val inputStream = response.body()?.byteStream()
            writeFileByOkio(inputStream, contentLength, file)

//        writeFile(inputStream, file)
        }
        taskEntity.status = STATUS_DONE
        listener.onCompletion(taskEntity)

        Log.e("tujiong", "task " + taskEntity.id + " completion")
    }

    fun makeException() {
        try {
            val url = URL("1234567890")
            url.protocol
        } catch (e: Exception) {
            Log.e("tujiong", "exception")
        } finally {
            Log.e("tujiong", "finally")
        }
    }

    fun getCacheFile(taskEntity: TaskEntity): File {
        val file = File(taskEntity.cachePath + "/" + taskEntity.id)
        if (!file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        if (!file.exists()) {
            file.createNewFile()
        }
        return file
    }

    fun writeFileByOkio(inputStream: InputStream?, contentLength: Long, file: File) {
        val source = Okio.source(inputStream)
        val sink = Okio.sink(file)
        var totalWrote: Float = 0.0f
        val taskSink = TaskSink(sink, contentLength, object : TaskSink.OnProgressListener {
            override fun onProgress(progress: Long, contentLength: Long) {
                totalWrote += progress
                val percent = totalWrote.div(contentLength)
                taskEntity.progress = percent
                listener.onProgress(taskEntity)
            }
        })
        val buffer = Okio.buffer(taskSink)
        buffer.writeAll(source)
        buffer.flush()
        source.close()
        taskSink.close()
        buffer.close()
    }

    fun writeFile(inputStream: InputStream?, file: File) {
        val outputStream = FileOutputStream(file)
        val bytes = ByteArray(1024 * 8)
        var length: Int = -1
//        while((length=inputStream.read(bytes))!=-1)
//        while ({ inputStream: InputStream?, bytes: ByteArray -> length = inputStream?.read(bytes) ?: 0;length }(inputStream, bytes) != -1) {
//            outputStream.write(bytes, 0, length)
//        }
        while ({ length = inputStream?.read(bytes) ?: 0;length }() != -1) {
            outputStream.write(bytes, 0, length)
        }
        outputStream.close()
        inputStream?.close()
    }
}