package com.example.pppb_t12

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pppb_t12.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mTodoDao: TodoDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = TodoRoomDatabase.getDatabase(this)
        mTodoDao = db!!.todoDao()!!

        with(binding) {
            btnAdd.setOnClickListener {
                val intent = Intent(this@MainActivity, TaskActivity::class.java)
                startActivity(intent)
            }

            mTodoDao.getRowCount().observe(this@MainActivity, Observer { count ->
                txtTaskCount.text = "$count pending tasks"
                if (count == 0) linNoTask.visibility = View.VISIBLE
                else linNoTask.visibility = View.GONE
            })

            val currentDate = Calendar.getInstance()
            val formatter = SimpleDateFormat("E, d MMM", Locale.getDefault())
            txtDate.text =  formatter.format(currentDate.time)
        }
    }

    override fun onResume() {
        super.onResume()
        getAllTodo()
    }

    private fun update(todo: Todo) {
        executorService.execute {
            mTodoDao.update(todo)
        }
    }

    private fun delete(todo: Todo) {
        executorService.execute {
            mTodoDao.delete(todo)
        }
    }

    private fun getAllTodo() {
        mTodoDao.allTodo.observe(this) {
            todos ->
            val adapterTodo = TodoAdapter(todos,
                { todo ->
                    val intent = Intent(this@MainActivity, TaskActivity::class.java)
                    intent.putExtra("id", todo.id)
                    intent.putExtra("title", todo.title)
                    intent.putExtra("tag", todo.tag)
                    intent.putExtra("status", todo.status)
                    intent.putExtra("description", todo.description)
                    startActivity(intent)
                },
                { todo ->
                    Toast.makeText(this@MainActivity, "Task deleted", Toast.LENGTH_SHORT).show()
                    delete(todo)
                },
                { todo ->
                    val newStatus = when(todo.status) {
                        "To do" -> "Doing"
                        "Doing" -> "Done"
                        "Done" -> "To do"
                        else -> ""
                    }
                    update(
                        Todo(
                            id = todo.id,
                            title = todo.title,
                            tag = todo.tag,
                            status = newStatus,
                            description = todo.description
                        )
                    )
                }
            )
            with(binding) {
                rvTodo.apply {
                    adapter = adapterTodo
                    layoutManager = LinearLayoutManager(this@MainActivity)
                }
            }
        }
    }
}