package com.team6.travel_app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.team6.travel_app.data.Comment
import com.team6.travel_app.data.CommentDao
import com.team6.travel_app.data.CommentDatabase
import com.team6.travel_app.model.Comments
import com.team6.travel_app.service.CommentAPI
import com.team6.travel_app.adapter.CommentAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class CommentViewModel(application: Application) : AndroidViewModel(application) {
    private val commentDao: CommentDao = CommentDatabase.invoke(application).commentDao()
    private val commentAPI: CommentAPI = createCommentAPI()
    private val _comments: MutableLiveData<List<Comments>> = MutableLiveData()
    val comments: LiveData<List<Comments>> = _comments

    companion object {
        const val COMMENT_URL = "http://nguyenvantuanolivas.mooo.com/p_api/"
    }

    private fun createCommentAPI(): CommentAPI {
        val retrofit = Retrofit.Builder()
            .baseUrl(COMMENT_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        return retrofit.create(CommentAPI::class.java)
    }
    fun getListComment(): List<Comment> {
        return commentDao.getComment()
    }
    fun getListCommentByProductId(productId: Int): List<Comment> {
        return commentDao.getCommentsByProductId(productId)
    }
    fun insertComment(comment: Comment) {
        viewModelScope.launch {
            commentDao.insertEntity(comment)
        }
        var newComment = commentDao.getComment()
        _comments.value = newComment.map { it.toCommentsAPI()}
        updateCommentsOnApi(newComment.map { it.toCommentsAPI() })
    }
    fun getItemCount(): Int {
        return commentDao.countComments()
    }
    fun insertComments(comments: List<Comment>) {
        viewModelScope.launch {
            commentDao.insertComments(comments)
        }
    }
    private fun Comments.toCommentRoom(): Comment {
        return Comment(
            id = this.id,
            commentText = this.commentText,
            productId = this.productId,
            custId = this.custId
        )
    }
    private fun Comment.toCommentsAPI(): Comments {
        return Comments(
            id = this.id,
            commentText = this.commentText,
            productId = this.productId,
            custId = this.custId
        )
    }
    private fun saveCommentsToDatabase(commentAPI: List<Comments>) {
        val comment = commentAPI.map { it.toCommentRoom() }
        commentDao.insertComments(comment)
    }
    fun updateCommentsOnApi(comments: List<Comments>) {
        val call = commentAPI.updateComments(comments)
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                } else {
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
            }
        })
    }
    fun fetchComments() {
        try {
            val response: Response<List<Comments>> = commentAPI.getComments()
            if (response.isSuccessful) {
                val commentsFromAPI: List<Comments>? = response.body()
                commentsFromAPI?.let { saveCommentsToDatabase(it) }
            } else {
                // Xử lý khi có lỗi từ phía server
            }
        } catch (e: Exception) {
            // Xử lý khi có lỗi trong quá trình gửi yêu cầu đến API
        }
    }
}