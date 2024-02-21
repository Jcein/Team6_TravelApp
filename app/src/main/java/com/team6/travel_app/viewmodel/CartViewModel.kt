package com.team6.travel_app.viewmodel

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team6.travel_app.adapter.CartAdapter
import com.team6.travel_app.data.Cart
import com.team6.travel_app.data.CartDatabase
import com.team6.travel_app.data.ImageDatabase
import com.team6.travel_app.data.ProductDatabase
import com.team6.travel_app.databinding.FragmentCartBinding
import com.team6.travel_app.model.Product
import com.team6.travel_app.model.Tour
import com.team6.travel_app.model.TourBaseClass
import com.team6.travel_app.model.TourP
import com.team6.travel_app.service.ToursAPI
import com.team6.travel_app.view.MainActivity
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.team6.travel_app.data.*
import kotlinx.coroutines.launch


class CartViewModel() : ViewModel() {
    private val viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private lateinit var binding: FragmentCartBinding
    val cartList = MutableLiveData<ArrayList<Cart>>()
    val _cartList get() = cartList
    val isCartListLoading = MutableLiveData<Boolean>()
    val isErrorOccurred = MutableLiveData<Boolean>()
    val tempList = ArrayList<Cart>()
    var isCartListEmpty = MutableLiveData<Boolean>()
    private var _totalAmounth = MutableLiveData<Int>()
    val totalAmounth: LiveData<Int> get() = _totalAmounth
    private lateinit var cartAdapter: CartAdapter
    private var _product = MutableLiveData<Product?>()
    val product get() = _product

    /*init {
         cartDao = CartDatabase.invoke(context).cartDao()
    }*/

    fun getDataFromRoom(
        context: Context,
        binding: FragmentCartBinding,
        cartDatabase: CartDatabase
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            cartDatabase.cartDao().getAllEntities().forEach {
                tempList.add(it) //TODO)
            }
        }
        cartList.value = tempList
        isCartListLoading.value = false
        isErrorOccurred.value = false
    }

    fun removeItemFromRoom(position: Int, cartDatabase: CartDatabase) =
        viewModelScope.launch(Dispatchers.IO) {
            isCartListEmpty.postValue(false)
            val request = cartDatabase.cartDao()
            println("before deletion the size is : ${request.rowCount()}")
            request.delete(position)
            calculateTotalAmount(cartDatabase)
            println("after deletion the size is : ${request.rowCount()}")

            if (request.rowCount() == 0) {
                isCartListEmpty.postValue(true)
            } else {
                isCartListEmpty.postValue(false)
            }
        }

    fun getIsCartListEmpty(): Boolean? {
        return isCartListEmpty.value
    }

    fun calculateTotalAmount(cartDatabase: CartDatabase) = viewModelScope.launch(Dispatchers.IO) {
        var total = 0
        _totalAmounth.postValue(0)
        val cartList = cartDatabase.cartDao().getAllEntities()
        cartList.forEach { cart ->
            println("the value of cartlist : $cartList")
            val quantity = cart.quantity
            val price = cart.price
            total += quantity * price!!
        }
        println("the value of temp total : $total")
        _totalAmounth.postValue(total)
        println("the value of total Amounth : ${_totalAmounth.value}")

    }


    fun updateQuantity(increaseQuantity : Boolean, id: Int, database: CartDatabase, holder: CartAdapter.PlaceHolder, bd: FragmentCartBinding) = viewModelScope.launch(Dispatchers.IO) {
        var quantity =  database.cartDao().getQuantity(id)
        if (increaseQuantity) {
            database.cartDao().updateQuantity(holder.binding.product?.id!!, ++quantity)
        } else if (!increaseQuantity && quantity > 1) {
            database.cartDao().updateQuantity(holder.binding.product?.id!!, --quantity)
        }

        CoroutineScope(Dispatchers.IO).launch {
            var total = 0
            val cartList = database.cartDao().getAllEntities()
            cartList.forEach { cart ->
                val quantity = cart.quantity
                val price = cart.price
                total += quantity * price!!
            }

            withContext(Dispatchers.Main) {
                bd.totalAmount.text = total.toString() + " đ"
            }
        }

        val price = database.cartDao().getPrice(holder.binding.product?.id!!)
        CoroutineScope(Dispatchers.Main).launch {
            holder.binding.productQuantityEditText.setText(quantity.toString())
            println("the edit text set to $quantity")
            //holder.binding.textViewProductPrice.text = ""
            holder.binding.textViewProductPrice.text = (quantity * price).toString() + " đ"
        }
    }
    fun onViewClicked(context : Context, id : Int) {
        viewModelScope.launch() {
            Log.i(TAG, "getClickedEntity: $id")
            val images = ImageDatabase(context = context).imageDao().getRecord(id)
            val item = ProductDatabase(context).productDao().getRecord(id)
            val product1 = Product(item.id,item.title,item.description,item.price,item.discountPercentage,item.rating,item.stock,item.brand,item.category,item.thumbnail,images)
            _product.value = product1
            Log.i(TAG, "onViewClicked: the value of product is : $product1")
            Log.i(TAG, "onViewClicked: ${_product.value}")
            Log.i(TAG, "onViewClicked: ${product.value}")

        }


    }


    fun getTotalAmount(): LiveData<Int>{
        return _totalAmounth
    }

    fun postRequest(context: Context, database: CartDatabase){
//        val tourp = TourP( 5, "2024-02-19", "TuanNV",4, true)
        val cusDb = CustomerDatabase.invoke(context).CustomerDao()
        val cusid = cusDb.getId()
        val retrofit = Retrofit
            .Builder()
            .baseUrl(MainActivity.TOUR_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ToursAPI::class.java)



        val cartList = database.cartDao().getAllEntities()
        cartList.forEach { cart ->
            val tourp = TourP(cart.quantity, "2024-02-21", cusid, cart.id, 1)
            val call = service.createPost(tourp)
            call.enqueue(object : Callback<TourBaseClass> {
                override fun onFailure(call: Call<TourBaseClass>, t: Throwable) {
                    t.printStackTrace()
                }
                override fun onResponse(
                    call: Call<TourBaseClass>,
                    response: Response<TourBaseClass>
                ) {
                    print("ok")
                }
            })
        }
        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, "thanh toán thành công", Toast.LENGTH_SHORT).show()
        }

    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }


}
