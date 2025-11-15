package com.example.reminderapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reminderapp.data.local.Reminder
import com.example.reminderapp.domain.useCases.DeleteUseCase
import com.example.reminderapp.domain.useCases.GetRemindersUseCase
import com.example.reminderapp.domain.useCases.InsertReminderUseCase
import com.example.reminderapp.domain.useCases.UpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val insertUseCase: InsertReminderUseCase,
    private val getRemindersUseCase: GetRemindersUseCase,
    private val deleteUseCase: DeleteUseCase,
    private val updateUseCase: UpdateUseCase
): ViewModel() {
    val uiState = getRemindersUseCase.invoke().map {
        ReminderUiState(it)
    }.stateIn(viewModelScope, started = SharingStarted.Eagerly, ReminderUiState())

    fun insert(reminder: Reminder) = viewModelScope.launch {
        insertUseCase.invoke(reminder)
    }

    fun delete(reminder: Reminder) = viewModelScope.launch {
        deleteUseCase.invoke(reminder)
    }

    fun update(reminder: Reminder) = viewModelScope.launch {
        updateUseCase.invoke(reminder)

    }
}
data class ReminderUiState(
    val list: List<Reminder> = emptyList()
)