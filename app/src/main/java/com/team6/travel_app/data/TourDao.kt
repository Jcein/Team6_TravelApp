package com.team6.travel_app.data

import android.database.Cursor
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery



@Dao
interface TourDao {
    @Insert
    suspend fun insertEntity(tour: Tour)
    @Insert
     fun insertAll(vararg tour: Tour): List<Long>

    @RawQuery
    fun getEntities(query : SupportSQLiteQuery) : List<Long>
    @Query("SELECT * FROM tour")
   /* var liveUsers: LiveData<MutableList<User?>?>? =
        rawDao.getUsers(SimpleSQLiteQuery("SELECT * FROM User ORDER BY name DESC"))*/
    fun getAllEntities() : List<Tour>

    @Query("SELECT * FROM tour WHERE id = :id")
    fun getEntity(id : Int) : Tour

    @Query("DELETE FROM tour WHERE id =  :id")
    suspend fun delete(id: Int) : Int
    @Query("SELECT id FROM tour WHERE id =  :id")
    fun searchForEntity(id: Int): Int
    @Query("UPDATE tour SET isChecked = 0 WHERE id = :id")
    fun updateEntity(id:Int)
    @Query("SELECT COUNT(*) FROM tour")
    fun rowCount() : Int
    @Query("SELECT isChecked FROM tour WHERE id = :id")
    fun isAddedToFavorite(id : Int) : Boolean
    @Query("SELECT * FROM tour")
    fun getCursorAll(): Cursor?

    @Query(value = "DELETE FROM tour")
    suspend fun deleteAllRecords()
    @Query(value = "SELECT * FROM tour")
    suspend fun getAllRecords(): List<Tour>
    @Query(value = "SELECT * FROM tour WHERE id = :id")
    suspend fun getRecord(id : Int) : Tour


}