package com.pratistha.reminderapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratistha.reminderapp.data.local.Medicine
import com.pratistha.reminderapp.domain.useCases.GetMedicineUseCase
import com.pratistha.reminderapp.domain.useCases.UpsertMedicineUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicineViewModel @Inject constructor(
    private val getMedicineUseCase: GetMedicineUseCase,
    private val upsertMedicineUseCase: UpsertMedicineUseCase
) : ViewModel() {

    val medicines: StateFlow<List<Medicine>> = getMedicineUseCase()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _editingMedicine = MutableStateFlow<Medicine?>(null)
    val editingMedicine = _editingMedicine.asStateFlow()

    fun startEditing(medicine: Medicine) {
        _editingMedicine.value = medicine
    }

    fun clearEditing() {
        _editingMedicine.value = null
    }

    fun upsertMedicine(medicine: Medicine, onComplete: () -> Unit) {
        viewModelScope.launch {
            upsertMedicineUseCase(medicine)
            onComplete()
        }
    }
}
