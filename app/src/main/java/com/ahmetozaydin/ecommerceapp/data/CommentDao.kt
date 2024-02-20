package com.team6.travel_app.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.team6.travel_app.model.Comments

@Dao
interface CommentDao {
    @Insert
    fun insertEntity(comment: Comment)
    @Query("SELECT * FROM comment")
    fun getComment() : List<Comment>
    @Query("SELECT * FROM comment WHERE productId = :productId")
    fun getCommentsByProductId(productId : Int) : List<Comment>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertComments(comments: List<Comment>)
    @Query("SELECT COUNT(*) FROM comment")
    fun countComments(): Int
}