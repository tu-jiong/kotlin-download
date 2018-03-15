package com.example.administrator.kotlindemo

import com.litesuits.orm.db.annotation.PrimaryKey
import com.litesuits.orm.db.annotation.Table
import com.litesuits.orm.db.enums.AssignType

/**
 * Created by Tujiong on 2017/5/23.
 */

@Table("TaskEntity")
data class TaskEntity(@PrimaryKey(AssignType.BY_MYSELF) var id: Int = 0,
                      var url: String = "",
                      var cachePath: String = "",
                      var progress: Float = 0.0f,
                      var status: Int = STATUS_WAIT)