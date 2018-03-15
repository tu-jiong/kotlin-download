package com.example.administrator.kotlindemo

import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.Message
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

/**
 * Created by Tujiong on 2017/5/23.
 */
object TaskManager : TaskListener {

    val type_start = 0
    val type_progress = 1
    val type_completion = 2
    val listeners: ArrayList<TaskListener> = ArrayList()
    val lists: ArrayList<TaskEntity> = ArrayList()
    val executor: ExecutorService = Executors.newFixedThreadPool(2)
    val handler: Handler

    init {
        handler = Handler(Looper.getMainLooper(),
                {
                    val taskEntity = it.obj
                    if (taskEntity is TaskEntity) {
                        when (it.what) {
                            type_start -> {
                                for (listener in listeners) {
                                    listener.onStart(taskEntity)
                                }
                            }
                            type_progress -> {
                                for (listener in listeners) {
                                    listener.onProgress(taskEntity)
                                }
                            }
                            type_completion -> {
                                for (listener in listeners) {
                                    listener.onCompletion(taskEntity)
                                }
                            }
                        }
                    }
                    false
                }
        )
    }

    fun addTask(entity: TaskEntity) {
        if (!lists.contains(entity)) {
            lists.add(entity)
            val taskRunnable = TaskRunnable(entity, this)
            entity.cachePath = getCachePath()
            executor.execute(taskRunnable)
        }
    }

    fun removeTask(entity: TaskEntity) {
        if (lists.contains(entity)) {
            lists.remove(entity)
        }
    }

    fun addTaskListener(listener: TaskListener) {
        if (!listeners.contains(listener)) {
            listeners.add(listener)
        }
    }

    fun removeTaskListener(listener: TaskListener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener)
        }
    }

    fun getTasks(): ArrayList<TaskEntity> {
        return lists
    }

    fun getCachePath(): String {
        return Environment.getExternalStorageDirectory().absolutePath + CACHE_PATH
    }

    override fun onStart(entity: TaskEntity) {
        val message = Message()
        message.what = type_start
        message.obj = entity
        handler.sendMessage(message)
    }

    override fun onProgress(entity: TaskEntity) {
        val message = Message()
        message.what = type_progress
        message.obj = entity
        handler.sendMessage(message)
    }

    override fun onCompletion(entity: TaskEntity) {
        removeTask(entity)
        entity.status = STATUS_DONE
        val message = Message()
        message.what = type_completion
        message.obj = entity
        handler.sendMessage(message)
    }
}