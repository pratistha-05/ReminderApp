package com.pratistha.reminderapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pratistha.reminderapp.data.local.dao.ReminderDao

@Database(entities = [Reminder::class], version = 2, exportSchema = false)
abstract class ReminderDatabase : RoomDatabase() {
    abstract fun dao(): ReminderDao
    companion object{
        @Volatile
        private var Instance:ReminderDatabase? = null
        fun getDatabase(context: Context): ReminderDatabase {
            return Instance?: synchronized(this){
                Room.databaseBuilder(context,ReminderDatabase::class.java,"reminder_db")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
        }
                .build()
    }
}
