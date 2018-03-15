package com.example.administrator.kotlindemo

import android.content.Context
import com.litesuits.orm.LiteOrm
import com.litesuits.orm.db.DataBaseConfig

/**
 * Created by Tujiong on 2017/5/25.
 */
object OrmManager {

    lateinit private var orm: LiteOrm

    fun init(context: Context, path: String) {
        val dataBaseConfig = DataBaseConfig(context)
        dataBaseConfig.dbName = path
        dataBaseConfig.dbVersion = 1
        orm = LiteOrm.newSingleInstance(dataBaseConfig)
        orm.setDebugged(true)
    }

    fun getLiteOrm(): LiteOrm {
        return orm
    }
}