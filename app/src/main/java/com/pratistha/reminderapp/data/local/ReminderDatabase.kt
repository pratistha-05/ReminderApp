package com.pratistha.reminderapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.pratistha.reminderapp.data.local.dao.ReminderDao

@Database(entities = [Reminder::class], version = 2)
abstract class ReminderDatabase : RoomDatabase() {

    abstract fun getReminderDao(): ReminderDao

    companion object {
        //migration for adding date variable in dao
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE Reminder ADD COLUMN date TEXT NOT NULL DEFAULT ''"
                )
            }
        }

        fun getInstance(context: Context) =
            Room.databaseBuilder(
                context,
                ReminderDatabase::class.java,
                "reminder"
            )
                .addMigrations(MIGRATION_1_2)
                .build()
    }
}
