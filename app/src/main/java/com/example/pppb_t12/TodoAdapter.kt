package com.example.pppb_t12

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.pppb_t12.databinding.ActivityTaskBinding
import com.example.pppb_t12.databinding.ItemTodoBinding

typealias OnClickTodo = (Todo) -> Unit
typealias OnLongClickTodo = (Todo) -> Unit
typealias OnClickStatus = (Todo) -> Unit

class TodoAdapter(
    private val listTodo: List<Todo>,
    private val onClickTodo: OnClickTodo,
    private val onLongClickTodo: OnLongClickTodo,
    private val onClickStatus: OnClickStatus

) : RecyclerView.Adapter<TodoAdapter.ItemTodoViewHolder>() {

    private lateinit var binding: ActivityTaskBinding

    inner class ItemTodoViewHolder(private val binding: ItemTodoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Todo) {
            with(binding) {
                txtTitle.text = data.title
                txtTag.text = data.tag
                txtStatus.text = data.status

                icTodo.setOnClickListener {
                    onClickStatus(data)
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
                onLongClickTodo(data)
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
}