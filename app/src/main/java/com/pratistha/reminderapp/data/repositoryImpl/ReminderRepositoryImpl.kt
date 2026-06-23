package com.pratistha.reminderapp.data.repositoryImpl

import com.google.firebase.firestore.FirebaseFirestore
import com.pratistha.reminderapp.data.local.Medicine
import com.pratistha.reminderapp.data.local.Reminder
import com.pratistha.reminderapp.data.local.dao.ReminderDao
import com.pratistha.reminderapp.domain.repository.ReminderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReminderRepositoryImpl @Inject constructor(private val reminderDao: ReminderDao): ReminderRepository {

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    private val medicinesFlow: Flow<List<Medicine>> by lazy {
        callbackFlow {
            val subscription = FirebaseFirestore.getInstance()
                .collection("medicines")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        close(error)
                        return@addSnapshotListener
                    }
                    if (snapshot != null) {
                        val medicines = snapshot.documents.mapNotNull {
                            it.toObject(Medicine::class.java)?.copy(id = it.id)
                        }
                        trySend(medicines)
                    }
                }
            awaitClose { subscription.remove() }
        }.shareIn(
            scope = repositoryScope,
            started = SharingStarted.WhileSubscribed(5000),
            replay = 1
        )
    }

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

    override fun getMedicines(): Flow<List<Medicine>> = medicinesFlow

    override suspend fun updateMedicineQuantity(name: String, dosage: Int, medicineId: String?): Long {
        val db = FirebaseFirestore.getInstance()
        val collection = db.collection("medicines")

        val docRef = if (medicineId != null) {
            collection.document(medicineId)
        } else {
            val snapshot = collection.whereEqualTo("name", name).get().await()
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
}
