package com.example.pppb_t12

import android.content.Context
import androidx.room.Database
import androidx.room.Room.databaseBuilder
import androidx.room.RoomDatabase

@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class TodoRoomDatabase : RoomDatabase() {
    abstract fun todoDao() : TodoDao?
    companion object {
        @Volatile
        private var INSTANCE: TodoRoomDatabase? = null
        fun getDatabase(context: Context) : TodoRoomDatabase ? {
            if (INSTANCE == null) {
                synchronized(TodoRoomDatabase::class.java) {
                    INSTANCE = databaseBuilder(
                        context.applicationContext,
                        TodoRoomDatabase::class.java,
                        "todo_database"
                    ).build()
                }
            }
            return INSTANCE
        }
    }
}