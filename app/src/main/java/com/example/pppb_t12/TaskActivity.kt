package com.example.pppb_t12

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.pppb_t12.databinding.ActivityTaskBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class TaskActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTaskBinding
    private lateinit var mTodoDao: TodoDao
    private lateinit var executorService: ExecutorService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = ActivityTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        executorService = Executors.newSingleThreadExecutor()
        val db = TodoRoomDatabase.getDatabase(this)
        mTodoDao = db!!.todoDao()!!

        with(binding) {
            val id = intent.getIntExtra("id", -1)
            if (id != -1) {
                edtTitle.setText(intent.getStringExtra("title"))
                edtTag.setText(intent.getStringExtra("tag"))
                edtDesc.setText(intent.getStringExtra("description"))
                txtTitle.text = "Captured Task"
                btnSave.text = "Save"
                btnSave.setOnClickListener {
                    if (edtTitle.text.toString() == "") {
                        Toast.makeText(this@TaskActivity, "Please input title", Toast.LENGTH_SHORT).show()
                    } else {
                        update(
                            Todo(
                                id = id,
                                title = edtTitle.text.toString(),
                                tag = edtTag.text.toString(),
                                status = intent.getStringExtra("status")!!,
                                description = edtDesc.text.toString()
                            )
                        )
                        Toast.makeText(this@TaskActivity, "Task saved", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            } else {
                btnSave.setOnClickListener {
                    if (edtTitle.text.toString() == "") {
                        Toast.makeText(this@TaskActivity, "Please input title", Toast.LENGTH_SHORT).show()
                    } else {
                        insert(
                            Todo(
                                title = edtTitle.text.toString(),
                                tag = edtTag.text.toString(),
                                status = "To do",
                                description = edtDesc.text.toString()
                            )
                        )
                        Toast.makeText(this@TaskActivity, "Task added", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                }
            }
        }
    }

    private fun insert(todo: Todo) {
        executorService.execute {
            mTodoDao.insert(todo)
        }
    }

    private fun update(todo: Todo) {
        executorService.execute {
            mTodoDao.update(todo)
        }
    }
}