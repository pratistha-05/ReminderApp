package com.example.reminderapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Reminder(
    val name:String,
    val dosage:String,
    @PrimaryKey(autoGenerate = false)
    val timeinMillis:Long,
    var isTaken:Boolean,
    val isRepeat:Boolean,
)
