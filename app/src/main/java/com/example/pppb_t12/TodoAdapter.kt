package com.example.pppb_t12

import android.content.Context
import android.graphics.Color
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pppb_t12.databinding.ActivityTaskBinding
import com.example.pppb_t12.databinding.ItemTodoBinding
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

typealias OnClickTodo = (Todo) -> Unit

class TodoAdapter(
    private val context: Context,
    private val listTodo: List<Todo>,
    private val onClickTodo: OnClickTodo,
) : RecyclerView.Adapter<TodoAdapter.ItemTodoViewHolder>() {

    private lateinit var binding: ActivityTaskBinding
    val db = TodoRoomDatabase.getDatabase(context)
    private var mTodoDao: TodoDao = db!!.todoDao()!!
    private var executorService: ExecutorService = Executors.newSingleThreadExecutor()

    inner class ItemTodoViewHolder(private val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Todo) {
            with(binding) {
                txtTitle.text = data.title
                txtTag.text = data.tag
                txtStatus.text = data.status

                icTodo.setOnClickListener {
                    val newStatus = when(data.status) {
                        "To do" -> "Doing"
                        "Doing" -> "Done"
                        "Done" -> "To do"
                        else -> ""
                    }
                    update(
                        Todo(
                            id = data.id,
                            title = data.title,
                            tag = data.tag,
                            status = newStatus,
                            description = data.description
                        )
                    )
                }

                when(data.status) {
                    "To do" -> {
                        txtStatus.background.setTint(ContextCompat.getColor(itemView.context, R.color.yellow_primary))
                        icTodo.background.setTint(ContextCompat.getColor(itemView.context, R.color.yellow_secondary))
                        icTodo.imageTintList = ContextCompat.getColorStateList(itemView.context, R.color.yellow_primary)
                        icTodo.setImageResource(R.drawable.ic_flag)
                    }
                    "Doing" -> {
                        txtStatus.background.setTint(ContextCompat.getColor(itemView.context, R.color.blue_primary))
                        icTodo.background.setTint(ContextCompat.getColor(itemView.context, R.color.blue_secondary))
                        icTodo.imageTintList = ContextCompat.getColorStateList(itemView.context, R.color.blue_primary)
                        icTodo.setImageResource(R.drawable.ic_run)
                    }
                    "Done" -> {
                        txtStatus.background.setTint(ContextCompat.getColor(itemView.context,R.color.green_primary))
                        icTodo.background.setTint(ContextCompat.getColor(itemView.context, R.color.green_secondary))
                        icTodo.imageTintList = ContextCompat.getColorStateList(itemView.context, R.color.green_primary)
                        icTodo.setImageResource(R.drawable.ic_check)
                    }
                    else -> {}
                }
            }

            itemView.setOnClickListener{
                onClickTodo(data)
            }

            itemView.setOnLongClickListener {
                Toast.makeText(itemView.context, "Task deleted", Toast.LENGTH_SHORT).show()
                delete(data)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTodoViewHolder {
        val binding = ItemTodoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemTodoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listTodo.size
    }

    override fun onBindViewHolder(holder: ItemTodoViewHolder, position: Int) {
        holder.bind(listTodo[position])
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
}