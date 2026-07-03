package com.pratistha.reminderapp.data.local

import androidx.annotation.Keep

@Keep
data class Medicine(
    val id: String = "",
    val userId: String = "",
    val name: String = "",
    val quantity: Int = 0,
    val purpose: String = "",
    val lowStockReminder: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
