package com.pratistha.reminderapp.data.local

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name:String,
    val medicineId: String? = null,
    val dosage:String,
    val slot: String = "",
    val timeinMillis:Long,
    var isTaken:Boolean,
    val isRepeat:Boolean,
    val frequency: String,
    val date: String,
)
