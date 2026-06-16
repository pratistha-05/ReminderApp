package com.pratistha.reminderapp.data.repositoryImpl

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.pratistha.reminderapp.data.local.Medicine
import com.pratistha.reminderapp.data.local.Reminder
import com.pratistha.reminderapp.data.local.dao.ReminderDao
import com.pratistha.reminderapp.domain.repository.ReminderRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Singleton

@Singleton
class ReminderRepositoryImpl(private val reminderDao: ReminderDao): ReminderRepository {
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

    override fun getMedicines(): Flow<List<Medicine>> = callbackFlow {
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
    }

    override suspend fun updateMedicineQuantity(name: String, dosage: Int) {
        val snapshot = FirebaseFirestore.getInstance()
            .collection("medicines")
            .whereEqualTo("name", name)
            .get()
            .await()

        for (doc in snapshot.documents) {
            doc.reference.update("quantity", FieldValue.increment(-dosage.toLong()))
        }
    }
}