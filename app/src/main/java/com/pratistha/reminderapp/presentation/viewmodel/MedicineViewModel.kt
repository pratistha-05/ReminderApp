package com.pratistha.reminderapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pratistha.reminderapp.data.local.Medicine
import com.pratistha.reminderapp.domain.useCases.DeleteUseCase
import com.pratistha.reminderapp.domain.useCases.GetMedicineUseCase
import com.pratistha.reminderapp.domain.useCases.GetRemindersUseCase
import com.pratistha.reminderapp.domain.useCases.InsertReminderUseCase
import com.pratistha.reminderapp.domain.useCases.UpdateUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MedicineViewModel @Inject constructor(
    private val getMedicineUseCase: GetMedicineUseCase,
) : ViewModel() {

    private val _medicines = MutableStateFlow<List<Medicine>>(emptyList())
    val medicines = _medicines.asStateFlow()

    fun getMedicines() {

        viewModelScope.launch {

            getMedicineUseCase.invoke()

                .collect { medicines ->

                    _medicines.value = medicines

                }
        }
    }
}