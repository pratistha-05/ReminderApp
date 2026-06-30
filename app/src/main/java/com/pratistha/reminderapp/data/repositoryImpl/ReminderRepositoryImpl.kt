package com.pratistha.reminderapp.data.repositoryImpl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.pratistha.reminderapp.data.local.Medicine
import com.pratistha.reminderapp.data.local.Reminder
import com.pratistha.reminderapp.data.local.dao.ReminderDao
import com.pratistha.reminderapp.domain.repository.ReminderRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderRepositoryImpl @Inject constructor(private val reminderDao: ReminderDao): ReminderRepository {

    override suspend fun insert(reminder: Reminder): Long {
        return reminderDao.insert(reminder)
    }

    override suspend fun delete(reminder: Reminder) {
        reminderDao.delete(reminder)
    }

    override suspend fun update(reminder: Reminder) {
        reminderDao.update(reminder)
    }

    override fun getAllReminders(): Flow<List<Reminder>> = reminderDao.getAllReminder()

    override fun getRemindersForDate(date: String): Flow<List<Reminder>> {
        return reminderDao.getRemindersByDate(date)
    }

    override suspend fun getLastDateForGroup(name: String, slot: String, frequency: String): String? =
        reminderDao.getLastDateForGroup(name, slot, frequency)

    override fun getMedicines(): Flow<List<Medicine>> = flow {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val snapshot = FirebaseFirestore.getInstance()
            .collection("medicines")
            .whereEqualTo("userId", userId)
            .get()
            .await()
        val medicines = snapshot.documents.mapNotNull {
            it.toObject(Medicine::class.java)?.copy(id = it.id)
        }
        emit(medicines)
    }.flowOn(Dispatchers.IO)

    override suspend fun updateMedicineQuantity(name: String, dosage: Int, medicineId: String?): Long {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("medicines")

        val docRef = if (medicineId != null) {
            collection.document(medicineId)
        } else {
            val snapshot = collection
                .whereEqualTo("userId", userId)
                .whereEqualTo("name", name)
                .get()
                .await()
            snapshot.documents.firstOrNull()?.reference
        }

        return docRef?.let { ref ->
            val doc = ref.get().await()
            val currentQuantity = doc.getLong("quantity") ?: 0L
            val newQuantity = (currentQuantity - dosage.toLong()).coerceAtLeast(0L)
            ref.update("quantity", newQuantity).await()
            newQuantity
        } ?: 0L
    }

    override suspend fun upsertMedicine(medicine: Medicine) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("medicines")

        val docRef = if (medicine.id.isNotEmpty()) {
            collection.document(medicine.id)
        } else {
            collection.document()
        }

        val medicineWithId = medicine.copy(id = docRef.id, userId = userId)
        docRef.set(medicineWithId).await()
    }
}
