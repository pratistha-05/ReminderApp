package com.pratistha.reminderapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.pratistha.reminderapp.data.local.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(data: Reminder)

    @Delete
    suspend fun delete(data: Reminder)

    @Update
    suspend fun update(data: Reminder)

    @Query("SELECT * FROM Reminder ORDER BY timeInMillis DESC")
    fun getAllReminder(): Flow<List<Reminder>>

    @Query("SELECT * FROM  Reminder WHERE date=:date ORDER BY timeInMillis ASC")
    fun getRemindersByDate(date: String): Flow<List<Reminder>>
}