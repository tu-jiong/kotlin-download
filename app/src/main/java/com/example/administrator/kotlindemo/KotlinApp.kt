package com.example.administrator.kotlindemo

import android.app.Application
import android.os.Environment
import android.util.Log

/**
 * Created by Tujiong on 2017/5/30.
 */
class KotlinApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.e("tujiong", "KotlinApp onCreate")
        OrmManager.init(this, Environment.getExternalStorageDirectory().absolutePath + DB_PATH)

        val liteOrm = OrmManager.getLiteOrm()
        Log.e("tujiong", "KotlinApp " + liteOrm.toString())
    }
}