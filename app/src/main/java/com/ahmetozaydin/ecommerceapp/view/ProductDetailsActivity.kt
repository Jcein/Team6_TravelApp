package com.team6.travel_app.view

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.team6.travel_app.adapter.ImageAdapter
import com.team6.travel_app.adapter.CommentAdapter
import com.team6.travel_app.data.Cart
import com.team6.travel_app.data.CartDatabase
import com.team6.travel_app.data.Favorite
import com.team6.travel_app.data.FavoriteDatabase
import com.team6.travel_app.data.Comment
import com.team6.travel_app.data.CommentDao
import com.team6.travel_app.viewmodel.CommentViewModel
import com.team6.travel_app.databinding.ActivityProductDetailsBinding
import com.team6.travel_app.model.Product
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class
ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding
    private var imageList = ArrayList<String>()
    private lateinit var imageAdapter: ImageAdapter
    private lateinit var commentDao: CommentDao
    private lateinit var commentAdapter: CommentAdapter
    private lateinit var commentViewModel: CommentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val products = intent.getParcelableExtra<Product>("product")
        if (products != null) {
            products.images?.forEach {
                imageList.add(it)
            }
        }
        if (products != null) {
            if (!imageList.contains(products.thumbnail)) {
                imageList.add(products.thumbnail.toString())
            }
        }
        binding.apply {
            textViewProductDescription.text = products?.description
            ("Số lượt đăng ký còn lại: " + products?.stock.toString()).also {
                textViewProductFeatures.text = it
            }
            (products?.price.toString() + " đ").also { textViewProductPrice.text = it }
            textViewProductName.text = products?.title
            ratingBar.rating = products?.rating!!.toString().toFloat()
            binding.backButton.setOnClickListener {
                finish()
            }
        }


        initializeRecyclerView()


        //val snapHelper :SnapHelper = PagerSnapHelper()
        val cartDatabase = CartDatabase.invoke(this)
        val favoriteDatabase = FavoriteDatabase.invoke(this)
        CoroutineScope(Dispatchers.IO).launch {
            if (products != null) {
                if (products.id?.let { cartDatabase.cartDao().isAddedToCart(it) } == true) {
                    runOnUiThread {
                        binding.checkBoxCart.isChecked = true
                    }
                }
            }
            if (products!!.id?.let {
                    favoriteDatabase.favoriteDao().isAddedToFavorite(it)
                } == true) {
                runOnUiThread {
                    binding.checkBoxFavorite.isChecked = true
                }
            }
        }

        binding.checkBoxCart.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                if (products!!.id?.let { it1 ->
                        cartDatabase.cartDao().isAddedToCart(
                            it1
                        )
                    } == true
                ) {
                    CoroutineScope(Dispatchers.IO).launch {
                        products!!.id.let { it1 ->
                            if (it1 != null) {
                                cartDatabase.cartDao().delete(it1)
                            }
                        }
                    }
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        cartDatabase.cartDao().insertEntity(
                            Cart(
                                products.id,
                                products.title,
                                products.discountPercentage,
                                products.description,
                                products.price,
                                products.rating,
                                products.stock,
                                products.brand,
                                products.thumbnail,
                                true,
                                1
                            )
                        )
                    }
                }
            }

        }
        binding.checkBoxFavorite.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                if (products!!.id?.let { it1 ->
                        favoriteDatabase.favoriteDao().isAddedToFavorite(
                            it1
                        )
                    } == true) {
                    CoroutineScope(Dispatchers.IO).launch {
                        products!!.id.let { it1 ->
                            if (it1 != null) {
                                favoriteDatabase.favoriteDao().delete(it1)
                            }
                        }
                    }
                } else {
                    CoroutineScope(Dispatchers.IO).launch {
                        favoriteDatabase.favoriteDao().insertEntity(
                            favorite = Favorite( //TODO(is there better structure)
                                products.id,
                                products.title,
                                products.description,
                                products.price,
                                products.rating,
                                products.stock,
                                products.discountPercentage,
                                products.brand,
                                products.category,
                                products.thumbnail,
                                isChecked = true
                            )
                        )
                    }
                }
            }
        }
        binding.buttonAddToShopping.setOnClickListener {
            if (binding.checkBoxCart.isChecked) {
                CoroutineScope(Dispatchers.IO).launch {
                    products!!.id?.let { it1 -> cartDatabase.cartDao().delete(it1) }
                }
                binding.checkBoxCart.isChecked = false
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    cartDatabase.cartDao().insertEntity(
                        Cart(
                            products!!.id,
                            products.title,
                            products.discountPercentage,
                            products.description,
                            products.price,
                            products.rating,
                            products.stock,
                            products.brand,
                            products.thumbnail,
                            true,
                            1
                        )
                    )
                }
                binding.checkBoxCart.isChecked = true
            }
        }
        binding.buttonAddComment.setOnClickListener{
            if (products != null) {
                val comment = Comment(commentViewModel.getItemCount()+1,binding.addCommentEditText.text.toString(),products.id,"")
                commentViewModel.insertComment(comment)
            }

        }
        binding.apply {
            val commentViewModel = ViewModelProvider(this@ProductDetailsActivity).get(CommentViewModel::class.java)
            commentViewModel.fetchComments()
            if (products != null) {
                val id = products.id!!
                commentAdapter = CommentAdapter(commentViewModel.getListCommentByProductId(id))
                binding.recyclerViewComments.layoutManager =
                    LinearLayoutManager(this@ProductDetailsActivity)
                binding.recyclerViewComments.adapter = commentAdapter
                commentViewModel.comments.observe(
                    this@ProductDetailsActivity,
                    Observer { comments ->
                        commentAdapter = CommentAdapter(commentViewModel.getListCommentByProductId(id))
                        recyclerViewComments.adapter = commentAdapter
                    })
            }
        }
    }
    private fun initializeRecyclerView() {
        val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerViewAllImages.layoutManager = layoutManager
        imageAdapter = ImageAdapter(imageList, this)
        binding.recyclerViewAllImages.adapter = imageAdapter
        PagerSnapHelper().attachToRecyclerView(binding.recyclerViewAllImages)

    }
}