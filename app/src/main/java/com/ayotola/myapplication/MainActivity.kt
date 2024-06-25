package com.ayotola.myapplication

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayotola.myapplication.databinding.ActivityMainBinding
import com.ayotola.myapplication.databinding.TodoItemBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val todoList = mutableListOf<String>()
    private lateinit var adapter: TodoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = TodoAdapter(todoList)
        binding.todoRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.todoRecyclerView.adapter = adapter


        binding.addButton.setOnClickListener {
            addTodoItem()
        }



        binding.filterInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterTodos(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun addTodoItem() {
        val todoText = binding.todoItem.text.toString()
        if (todoText.isNotEmpty()) {
            todoList.add(todoText)
            adapter.notifyDataSetChanged()
            binding.todoItem.text.clear()
        }
    }


    private fun filterTodos(query: String) {
        val filteredList = todoList.filter { it.contains(query, true) }
        adapter.updateList(filteredList)
    }


    private fun editTodoItem(position: Int) {
        val todoText = todoList[position]
        val editText = EditText(this)
        editText.setText(todoText)

        AlertDialog.Builder(this)
            .setTitle("Edit TODO")
            .setView(editText)
            .setPositiveButton("Save") { dialog, _ ->
                todoList[position] = editText.text.toString()
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }
            .setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }
            .show()
    }





    inner class TodoAdapter(private var items: List<String>) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

        inner class TodoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            val todoText: TextView = view.findViewById(R.id.todoText)
            val editButton: ImageButton = view.findViewById(R.id.editButton)
            val deleteButton: ImageButton = view.findViewById(R.id.deleteButton)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
            val binding = TodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            return TodoViewHolder(binding.root)
        }

        override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
            val item = items[position]
            holder.todoText.text = item
            holder.editButton.setOnClickListener { editTodoItem(position) }
            holder.deleteButton.setOnClickListener {
                todoList.removeAt(position)
                notifyDataSetChanged()
            }
        }

        override fun getItemCount() = items.size

        fun updateList(newItems: List<String>) {
            items = newItems
            notifyDataSetChanged()
        }
    }

}
