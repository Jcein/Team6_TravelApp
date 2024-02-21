package com.team6.travel_app.data

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface CustomerDao {
    @Insert
    suspend fun insertEntity(customer: Customer)
    @Insert
    fun insertAll(vararg customer: Customer): List<Long>
    @RawQuery
    fun getEntities(query : SupportSQLiteQuery) : List<Long>

    @Query("SELECT * FROM customer")
    fun getEntitie() : List<Customer>

    @Query("DELETE FROM customer")
    suspend fun delete() : Int

    @Query("SELECT id FROM customer")
    fun getId(): String

    @Query("SELECT password FROM customer")
    fun getPassword(): Int

    @Query("SELECT cusName FROM customer ")
    fun getName() : String

    @Query("SELECT email FROM customer ")
    fun getEmail() : Int

    @Query("SELECT phoneNumber FROM customer ")
    fun getPhoneNumber() : Int

    @Query("SELECT address FROM customer ")
    fun getAddress() : Int

    @Query("SELECT COUNT(*) FROM customer")
    fun rowCount() : Int

    @Query(value = "DELETE FROM customer")
    suspend fun deleteAllRecords()

}