package com.example.administrator.kotlindemo

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView

class TaskActivity : AppCompatActivity(), TaskListener {

    lateinit private var recyclerView: RecyclerView
    lateinit private var adapter: TaskAdapter
    lateinit private var tasks: ArrayList<TaskEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)
        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        val manager = LayoutManagerWrapper(this)
        recyclerView.layoutManager = manager
        adapter = TaskAdapter()
        recyclerView.adapter = adapter

        TaskManager.addTaskListener(this)
        tasks = TaskManager.getTasks()
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        TaskManager.removeTaskListener(this)
    }

    override fun onStart(entity: TaskEntity) {
        val liteOrm = OrmManager.getLiteOrm()
        liteOrm.save(entity)
    }

    override fun onProgress(entity: TaskEntity) {
        tasks.forEachIndexed { index, taskEntity ->
            taskEntity.also {
                if (taskEntity.id == entity.id) {
                    adapter.notifyItemChanged(index)
                }
            }
        }
    }

    override fun onCompletion(entity: TaskEntity) {
        tasks.remove(entity)
        adapter.notifyDataSetChanged()
        val liteOrm = OrmManager.getLiteOrm()
        Log.e("tujiong", "TaskActivity liteOrm " + liteOrm.toString())
        liteOrm.save(entity)
        val list = liteOrm.query(TaskEntity::class.java)
        Log.e("tujiong", "TaskActivity db \n" + list.toString())
    }

    inner class TaskAdapter : RecyclerView.Adapter<TaskHolder>() {
        override fun onBindViewHolder(holder: TaskHolder?, position: Int) {
            val task = tasks[position]
            holder?.taskId?.text = "任务编号 : ${task.id} "
            when (task.status) {
                STATUS_START -> {
                    holder?.taskStatus?.text = "任务中..."
                }
                STATUS_WAIT -> {
                    holder?.taskStatus?.text = "任务等待"
                }
                STATUS_DONE -> {
                    holder?.taskStatus?.text = "任务完成"
                }
            }
            holder?.taskProgress?.progress = (task.progress * 100).toInt()
        }

        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): TaskHolder {
            val view = View.inflate(parent?.context, R.layout.task_item, null)
            return TaskHolder(view)
        }

        override fun getItemCount(): Int {
            return tasks.size
        }
    }

    inner class TaskHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var taskId: TextView = itemView.findViewById(R.id.task_id) as TextView
        var taskStatus: TextView = itemView.findViewById(R.id.task_status) as TextView
        var taskProgress: ProgressBar = itemView.findViewById(R.id.task_progress) as ProgressBar
    }

    class LayoutManagerWrapper(context: Context) : LinearLayoutManager(context) {

        override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State) {
            try {
                super.onLayoutChildren(recycler, state)
            } catch (e: IndexOutOfBoundsException) {
                Log.e("Error", "IndexOutOfBoundsException in RecyclerView happens")
            }
        }
    }
}