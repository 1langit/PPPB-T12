package com.example.pppb_t12

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(todo: Todo)

    @Update
    fun update(todo: Todo)

    @Delete
    fun delete(todo: Todo)

    @get:Query("SELECT * FROM todo_table ORDER BY id ASC")
    val allTodo: LiveData<List<Todo>>

    @Query("SELECT COUNT(*) FROM todo_table")
    fun getRowCount(): LiveData<Int>
}