package com.example.pppb_t12

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pppb_t12.databinding.ActivityMainBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var mTodoDao: TodoDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
            })

            val currentDate = LocalDate.now()
            val formatter = DateTimeFormatter.ofPattern("E, d MMM", Locale.getDefault())
            txtDate.text =  currentDate.format(formatter)
        }
    }

    override fun onResume() {
        super.onResume()
        getAllTodo()
    }

    private fun getAllTodo() {
        mTodoDao.allTodo.observe(this) {
            todos ->
            val adapterTodo = TodoAdapter(this, todos) {
                todo ->
                val intent = Intent(this@MainActivity, TaskActivity::class.java)
                intent.putExtra("id", todo.id)
                intent.putExtra("title", todo.title)
                intent.putExtra("tag", todo.tag)
                intent.putExtra("status", todo.status)
                intent.putExtra("description", todo.description)
                startActivity(intent)
            }
            with(binding) {
                rvTodo.apply {
                    adapter = adapterTodo
                    layoutManager = LinearLayoutManager(this@MainActivity)
                }
            }
        }
    }
}