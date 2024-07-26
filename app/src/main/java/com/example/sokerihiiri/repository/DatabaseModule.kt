package com.example.sokerihiiri.repository

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): SokerihiiriDatabase {
        return Room.databaseBuilder(
            context,
            SokerihiiriDatabase::class.java,
            "sokerihiiri_database"
        )
            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
            .build()
    }

    @Provides
    fun provideBloodSugarMeasurementDao(database: SokerihiiriDatabase): BloodSugarMeasurementDao {
        return database.bloodSugarMeasurementDao()
    }

    @Provides
    fun provideInsulinInjectionDao(database: SokerihiiriDatabase): InsulinInjectionDao {
        return database.insulinInjectionDao()
    }

    @Provides
    fun provideMealDao(database: SokerihiiriDatabase): MealDao {
        return database.mealDao()
    }

    @Provides
    @Singleton
    fun provideSokerihiiriRepository(
        bloodSugarMeasurementDao: BloodSugarMeasurementDao,
        insulinInjectionDao: InsulinInjectionDao,
        mealDao: MealDao
    ): SokerihiiriRepository {
        return SokerihiiriRepository(bloodSugarMeasurementDao, insulinInjectionDao, mealDao)
    }
}