package com.example.administrator.kotlindemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.Adapter
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class HomeActivity : AppCompatActivity(), TaskListener {

    lateinit private var recyclerView: RecyclerView
    lateinit private var button: Button
    lateinit private var adapter: HomeAdapter
    lateinit private var lists: ArrayList<TaskEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        recyclerView = findViewById(R.id.recycler_view) as RecyclerView
        val manager = LinearLayoutManager(this)
        recyclerView.layoutManager = manager
        adapter = HomeAdapter()
        recyclerView.adapter = adapter

        button = findViewById(R.id.button) as Button
        button.setOnClickListener { _ ->
            val intent = Intent(this, TaskActivity::class.java)
            startActivity(intent)
        }

        lists = ArrayList(30)

        for (index in 0 until 30) {
            val entity = TaskEntity()
            entity.id = index
            entity.url = TESTURL
            lists.add(entity)
        }

        adapter.notifyDataSetChanged()

        val liteOrm = OrmManager.getLiteOrm()
        Log.e("tujiong", "HomeActivity " + liteOrm.toString())

        TaskManager.addTaskListener(this)

        val b = ParentClass.B("")
        b.name = "joy"
        Log.e("tujiong", b.name)
    }

    override fun onStart(entity: TaskEntity) {
        lists.forEachIndexed { index, taskEntity ->
            taskEntity.also {
                if (taskEntity.id == entity.id) {
                    adapter.notifyItemChanged(index)
                }
            }
        }
    }

    override fun onProgress(entity: TaskEntity) {
    }

    override fun onCompletion(entity: TaskEntity) {
        lists.forEachIndexed { index, taskEntity ->
            taskEntity.also {
                if (taskEntity.id == entity.id) {
                    adapter.notifyItemChanged(index)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        TaskManager.removeTaskListener(this)
    }

    inner class HomeAdapter : Adapter<HomeHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): HomeHolder {
            val view = View.inflate(parent?.context, R.layout.home_item, null)
            return HomeHolder(view)
        }

        override fun getItemCount(): Int {
            return lists.size
        }

        override fun onBindViewHolder(holder: HomeHolder?, position: Int) {
            holder?.itemView?.tag = position
            val task = lists[position]
            holder?.taskId?.text = "任务编号 : ${task.id}"
            if (task.status == STATUS_DONE) {
                holder?.taskStatus?.text = "任务完成"
                holder?.taskPath?.visibility = View.VISIBLE
                holder?.taskPath?.text = "文件路径 : ${task.cachePath} "
            } else {
                holder?.taskStatus?.text = ""
                holder?.taskPath?.visibility = View.GONE
            }
        }
    }

    inner class HomeHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var taskId: TextView = itemView.findViewById(R.id.task_id) as TextView
        var taskPath: TextView = itemView.findViewById(R.id.task_path) as TextView
        var taskStatus: TextView = itemView.findViewById(R.id.task_status) as TextView

        init {
            taskPath.visibility = View.GONE
            itemView.setOnClickListener { view ->
                val position: Int = view.tag as Int
                val entity: TaskEntity = lists[position]
                TaskManager.addTask(entity)
                Toast.makeText(this@HomeActivity, " task added ", Toast.LENGTH_SHORT).show()
            }
        }
    }
}