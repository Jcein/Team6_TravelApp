package com.team6.travel_app.viewmodel

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.team6.travel_app.data.Cart
import com.team6.travel_app.data.CartDatabase

import com.team6.travel_app.data.CustomerDatabase
import com.team6.travel_app.data.Image
import com.team6.travel_app.data.ImageDatabase
import com.team6.travel_app.data.ProductDatabase
import com.team6.travel_app.databinding.FragmentCartBinding
import com.team6.travel_app.databinding.FragmentLogInBinding
import com.team6.travel_app.model.BaseClass
import com.team6.travel_app.model.CusBaseClass

import com.team6.travel_app.model.Customer
import com.team6.travel_app.model.TourBaseClass
import com.team6.travel_app.model.TourP
import com.team6.travel_app.service.CusAPI
import com.team6.travel_app.service.ProductsAPI
import com.team6.travel_app.service.ToursAPI
import com.team6.travel_app.utils.CustomSharedPreferences
import com.team6.travel_app.view.LogInActivity
import com.team6.travel_app.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CustomerViewModel : ViewModel() {
    private val _cus = MutableLiveData<ArrayList<Customer>?>()
    val cus get() = _cus
    private var customPreferences = CustomSharedPreferences()
    private var refreshTime = 0.01 * 60 * 1000 * 1000 * 1000L
    var rowCount = 0


    // Các phương thức
// getData
    fun getData(context: Context, acc: String, pass:String) {
        val updateTime = customPreferences.getTime()
        //Log.i(TAG, "$refreshTime  getData: "+(System.nanoTime() - updateTime!!))
        if (updateTime != null && updateTime != 0L && System.nanoTime() - updateTime < refreshTime) {
            //getDataFromSQLite(context)
            print("ok")
        }else{
            getDataFromAPI(context, acc, pass)

        }
    }


    fun message(context: Context) {
        Toast.makeText(context,"tài khoản hoặc mật khẩu sai",Toast.LENGTH_SHORT).show()
    }

    // getDataFromApi
    private fun getDataFromAPI(context: Context, acc: String, pass:String){
//        Toast.makeText(context,"Đăng nhập thành công",Toast.LENGTH_SHORT).show()
        val retrofit = Retrofit
            .Builder()
            .baseUrl(LogInActivity.CUS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(CusAPI::class.java)
        val call = service.getData(acc, pass)

        call.enqueue(object : Callback<CusBaseClass> {
            override fun onFailure(call: Call<CusBaseClass>, t: Throwable) {
                t.printStackTrace()

            }
            override fun onResponse(
                call: Call<CusBaseClass>,
                response: Response<CusBaseClass>
            ) {
                response.body()?.let {
                    println("onResponse is worked")

                    it.customer?.let { it1 -> storeInSQLite(context, it1 as ArrayList<Customer>) }
                    _cus.postValue(it.customer as ArrayList<Customer>?)
                }
            }
        })
    }

    // Luu vào SQLite
    fun storeInSQLite(context: Context, cus: ArrayList<Customer>) {
        println("storeInSQLLite")
        viewModelScope.launch {
            val cusDb = CustomerDatabase.invoke(context).CustomerDao()
            cusDb.deleteAllRecords()
            rowCount =0
            cus.forEach {
                val aCus = com.team6.travel_app.data.Customer(it.id,it.password,it.cusName,it.email,it.phoneNumber,it.address)
                cusDb.insertEntity(aCus)
                rowCount+=1
                Toast.makeText(context,"Đăng nhập thành công",Toast.LENGTH_SHORT).show()
            }
//            Toast.makeText(context,"Đăng nhập thành công",Toast.LENGTH_SHORT).show()
        }

//        customPreferences.saveTime(System.nanoTime())
    }

    fun postRequest(context: Context, account:String, password:String, name:String, email:String, phone:String, address:String ){

        val retrofit = Retrofit
            .Builder()
            .baseUrl(LogInActivity.CUS_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(CusAPI::class.java)

        val cust = Customer(account, password, name, email, phone, address)
        val call = service.createPost(cust)
        call.enqueue(object : Callback<CusBaseClass> {
            override fun onFailure(call: Call<CusBaseClass>, t: Throwable) {
                t.printStackTrace()
            }
            override fun onResponse(
                call: Call<CusBaseClass>,
                response: Response<CusBaseClass>
            ) {
                print("ok")
            }
        })

        CoroutineScope(Dispatchers.Main).launch {
            Toast.makeText(context, "Đăng ký thành công", Toast.LENGTH_SHORT).show()
        }

    }

// Lấy dữ liệu từ SQLite
}