package com.pratistha.reminderapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pratistha.reminderapp.data.local.dao.ReminderDao


@Database(entities = [Reminder::class], version = 4)
abstract class ReminderDatabase : RoomDatabase() {

    abstract fun getReminderDao(): ReminderDao

    companion object {
        fun getInstance(context: Context) =
            Room.databaseBuilder(
                context,
                ReminderDatabase::class.java,
                "reminder"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}
