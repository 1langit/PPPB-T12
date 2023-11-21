package com.example.pppb_t12

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todo_table")
data class Todo(
    @PrimaryKey(autoGenerate = true)
    @NonNull
    val id: Int = 0,

    @ColumnInfo(name = "title")
    val title: String = "",

    @ColumnInfo(name = "tag")
    val tag: String = "",

    @ColumnInfo(name = "status")
    val status: String = "",

    @ColumnInfo(name = "description")
    val description: String = ""
)
