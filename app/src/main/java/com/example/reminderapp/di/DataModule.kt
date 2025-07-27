package com.example.reminderapp.di

import android.content.Context
import com.example.reminderapp.data.local.ReminderDatabase
import com.example.reminderapp.data.local.dao.ReminderDao
import com.example.reminderapp.domain.repository.ReminderRepository
import com.example.reminderapp.data.repositoryImpl.ReminderRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context):ReminderDatabase {
       return ReminderDatabase.getInstance(context)
    }

    @Provides
    fun provideDao(reminderDatabase:ReminderDatabase): ReminderDao {
        return reminderDatabase.getReminderDao()
    }

    @Provides
    fun provideReminderRepo(reminderDao:ReminderDao):ReminderRepository{
        return ReminderRepositoryImpl(reminderDao)
    }
}