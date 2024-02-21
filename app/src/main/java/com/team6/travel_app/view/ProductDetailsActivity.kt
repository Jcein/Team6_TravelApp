package com.team6.travel_app.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.team6.travel_app.adapter.ImageAdapter
import com.team6.travel_app.data.Cart
import com.team6.travel_app.data.CartDatabase
import com.team6.travel_app.data.Tour
import com.team6.travel_app.data.TourDatabase
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
            ("Số lượt đăng ký còn lại: " + products?.stock.toString()).also { textViewProductFeatures.text = it }
            (products?.price.toString()+" đ").also { textViewProductPrice.text = it }
            textViewProductName.text = products?.title
            ratingBar.rating = products?.rating!!.toString().toFloat()
            binding.backButton.setOnClickListener {
                finish()
            }
        }


        initializeRecyclerView()


        //val snapHelper :SnapHelper = PagerSnapHelper()
        val cartDatabase = CartDatabase.invoke(this)
        val tourDatabase = TourDatabase.invoke(this)
        CoroutineScope(Dispatchers.IO).launch {
            if (products != null) {
                if (products.id?.let { cartDatabase.cartDao().isAddedToCart(it) } == true) {
                    runOnUiThread {
                        //binding.checkBoxCart.isChecked = true
                    }
                }
            }
//            if (products!!.id?.let { tourDatabase.tourDao().isAddedToFavorite(it) } == true) {
//                runOnUiThread {
//                    binding.checkBoxFavorite.isChecked = true
//                }
//            }
        }

//        binding.checkBoxCart.setOnClickListener {
        binding.buttonAddToShopping.setOnClickListener {
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

//            CoroutineScope(Dispatchers.IO).launch {
//                if (products!!.id?.let { it1 ->
//                        tourDatabase.tourDao().isAddedToFavorite(
//                            it1
//                        )
//                    } == true) {
//                    CoroutineScope(Dispatchers.IO).launch {
//                        products!!.id.let { it1 ->
//                            if (it1 != null) {
//                                tourDatabase.tourDao().delete(it1)
//                            }
//                        }
//                    }
//                } else {
//                    CoroutineScope(Dispatchers.IO).launch {
//                        tourDatabase.tourDao().insertEntity(
//                            tour = Tour( //TODO(is there better structure)
//                                products.id,
//                                products.title,
//                                products.description,
//                                products.price,
//                                7,
//                                products.stock,
//                                products.discountPercentage,
//                                products.brand,
//                                products.category,
//                                products.thumbnail,
//                                isChecked = true
//                            )
//                        )
//                    }
//                }
//            }
//        }
//        binding.buttonAddToShopping.setOnClickListener {
//            if(binding.checkBoxCart.isChecked){
//                CoroutineScope(Dispatchers.IO).launch {
//                    products!!.id?.let { it1 -> cartDatabase.cartDao().delete(it1) }
//                }
////                binding.checkBoxCart.isChecked = false
//            }else{
//                CoroutineScope(Dispatchers.IO).launch {
//                    cartDatabase.cartDao().insertEntity(
//                        Cart(
//                            products!!.id,
//                            products.title,
//                            products.discountPercentage,
//                            products.description,
//                            products.price,
//                            products.rating,
//                            products.stock,
//                            products.brand,
//                            products.thumbnail,
//                            true,
//                            1
//                        )
//                    )
//                }
////                binding.checkBoxCart.isChecked = true
//            }
//        }
    }

    private fun initializeRecyclerView() {
        val layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        binding.recyclerViewAllImages.layoutManager = layoutManager
        imageAdapter = ImageAdapter(imageList, this)
        binding.recyclerViewAllImages.adapter = imageAdapter
        PagerSnapHelper().attachToRecyclerView(binding.recyclerViewAllImages)

    }
/*
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.home -> {
                NavUtils.navigateUpFromSameTask(this)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }*/
}