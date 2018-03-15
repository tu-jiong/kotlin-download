package com.example.administrator.kotlindemo

/**
 * Created by Tujiong on 2017/5/24.
 */
interface TaskListener {
    fun onStart(entity: TaskEntity)
    fun onProgress(entity: TaskEntity)
    fun onCompletion(entity: TaskEntity)
}