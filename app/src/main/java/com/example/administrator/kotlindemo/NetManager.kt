package com.example.administrator.kotlindemo

import okhttp3.OkHttpClient

/**
 * Created by Tujiong on 2017/5/30.
 */
object NetManager {

    private val okHttpClient: OkHttpClient = OkHttpClient()

    fun getOkHttpClient(): OkHttpClient {
        return okHttpClient
    }
}