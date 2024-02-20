package com.team6.travel_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.team6.travel_app.model.Comments
import com.team6.travel_app.data.Comment
import com.team6.travel_app.data.CommentDao
import com.team6.travel_app.service.CommentAPI
import com.team6.travel_app.databinding.EachCommentBinding
import retrofit2.Response

class CommentAdapter(private var comments: List<Comment>, ) : RecyclerView.Adapter<CommentAdapter.ViewHolder>() {
    private lateinit var commentDao: CommentDao
    private lateinit var commentApi: CommentAPI

    inner class ViewHolder(private val binding: EachCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) {
            binding.custIdTextView.text = comment.custId
            binding.commentTextView.text = comment.commentText
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = EachCommentBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val comment = comments[position]
        holder.bind(comment)
    }

    override fun getItemCount(): Int {
        return comments.size
    }
    suspend fun newComment(comment: Comment){
        commentDao.insertEntity(comment)
    }
    fun updateCommentsAPI(newComments: List<Comments>) {
        val commentAPI = newComments.map { it.toCommentRoom() }
        comments = commentAPI
        notifyDataSetChanged()
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
}