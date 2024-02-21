package com.team6.travel_app.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Customer::class], version = 1)
abstract class CustomerDatabase : RoomDatabase(){
    abstract fun CustomerDao() : CustomerDao
    companion object{
        //Singleton
        @Volatile private var instance : CustomerDatabase? = null
        private val lock = Any()
        operator fun invoke(context : Context) = instance ?: synchronized(lock) {
            instance ?: makeDatabase(context).also {
                instance = it
            }
        }
        private fun makeDatabase(context : Context) = Room.databaseBuilder(
            context.applicationContext,CustomerDatabase::class.java,"customer"
        ).build()
    }
}