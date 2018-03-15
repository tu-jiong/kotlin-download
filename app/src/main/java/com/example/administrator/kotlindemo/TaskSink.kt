package com.example.administrator.kotlindemo

import okio.Buffer
import okio.ForwardingSink
import okio.Sink

/**
 * Created by Tujiong on 2017/6/1.
 */
class TaskSink(sink: Sink) : ForwardingSink(sink) {

    var contentLength: Long = 0
    lateinit var listener: OnProgressListener

    constructor(sink: Sink, contentLength: Long, listener: OnProgressListener) : this(sink) {
        this.contentLength = contentLength
        this.listener = listener
    }

    override fun write(source: Buffer?, byteCount: Long) {
        super.write(source, byteCount)
        listener.onProgress(byteCount, contentLength)
    }

    interface OnProgressListener {
        fun onProgress(progress: Long, contentLength: Long)
    }
}