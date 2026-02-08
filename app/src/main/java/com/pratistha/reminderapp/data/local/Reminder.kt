package com.pratistha.reminderapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name:String,
    val dosage:String,
    val slot: String = "",
    val timeinMillis:Long,
    var isTaken:Boolean,
    val isRepeat:Boolean,
    val frequency: String= Frequency.Daily.value,
    val date: String,
)
