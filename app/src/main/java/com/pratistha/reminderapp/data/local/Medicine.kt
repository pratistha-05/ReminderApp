package com.pratistha.reminderapp.data.local

data class Medicine(
    val id: String = "",
    val name: String = "",
    val quantity: Int = 0,
    val purpose: String = "",
    val lowStockReminder: Boolean = true,
    val dosagePerUse: Int = 1,
    val createdAt: Long = System.currentTimeMillis()
)