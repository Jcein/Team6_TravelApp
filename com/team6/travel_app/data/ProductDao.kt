package com.team6.travel_app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ProductDao {
    @Insert
    suspend fun insertEntity(product: Product)
    @Query(value = "DELETE FROM product")
    suspend fun deleteAllRecords()
    @Query(value = "SELECT * FROM product")
    suspend fun getAllRecords(): List<Product>
    @Query(value = "SELECT * FROM product WHERE id = :id")
    suspend fun getRecord(id : Int) : Product
}