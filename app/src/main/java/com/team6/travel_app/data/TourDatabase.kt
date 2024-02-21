package com.team6.travel_app.data

import android.content.Context
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [Tour::class], version = 3)
abstract class TourDatabase : RoomDatabase(){
abstract fun tourDao() : TourDao
companion object{
    //Singleton
    @Volatile private var instance : TourDatabase? = null
    private val lock = Any()
    operator fun invoke(context : Context) = instance ?: synchronized(lock) {
        instance ?: makeDatabase(context).also {
            instance = it
        }
    }
    private fun makeDatabase(context : Context) = Room.databaseBuilder(
        context.applicationContext,TourDatabase::class.java,"tour"
    ).fallbackToDestructiveMigration().build()
}
}