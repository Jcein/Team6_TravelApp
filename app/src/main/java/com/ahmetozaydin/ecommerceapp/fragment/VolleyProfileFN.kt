package com.ahmetozaydin.ecommerceapp.fragment

import android.content.Context
import android.widget.TextView
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class VolleyProfileFn {
    var strJSON="";
    fun getJSONArrayObjects(context: Context, textView: TextView){
        val queue= Volley.newRequestQueue(context)
        //2 tao url
        val url="http://nguyenvantuanolivas.mooo.com/api2/customer"
        //3 goi request(mang cua cac doi tuong)
        //JsonArrayRequest(url, thanhcong, thatbai)
        val request=JsonArrayRequest(url,
            Response.Listener {
                    response ->
                for(i in 0 until response.length()){
                    val person=response.getJSONObject(i)
                    val cus_name=person.getString("cus_name")
                    val phone_number=person.getString("phone_number")
                    val email=person.getString("email")
                    val address=person.getString("address")
                    strJSON+="cus_name: $cus_name\n"
                    strJSON+="phone_number: $phone_number\n"
                    strJSON+="email: $email\n"
                    strJSON+="address: $address\n"
                }
                textView.text = strJSON
            },
            Response.ErrorListener {
                    error ->  textView.text = error.message
            } )
        //4thuc thi request
        queue.add(request)
    }
}
