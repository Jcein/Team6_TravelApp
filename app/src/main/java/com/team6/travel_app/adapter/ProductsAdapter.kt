package com.team6.travel_app.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.team6.travel_app.R
import com.team6.travel_app.data.Cart
import com.team6.travel_app.data.CartDatabase
import com.team6.travel_app.data.Favorite
import com.team6.travel_app.data.FavoriteDatabase
import com.team6.travel_app.databinding.EachProductBinding
import com.team6.travel_app.model.Product
import com.team6.travel_app.utils.Utils
import com.team6.travel_app.utils.downloadFromUrl
import com.team6.travel_app.utils.placeholderProgressBar
import com.team6.travel_app.view.ProductDetailsActivity
import kotlinx.coroutines.*


class ProductsAdapter(
    private val products: ArrayList<Product>,
    private val context: Context,
) : RecyclerView.Adapter<ProductsAdapter.PlaceHolder>() {
    private var favoriteDatabase: FavoriteDatabase? = null
    private var favorite: Favorite? = null
    private var cartDatabase: CartDatabase? = null

    interface Listener {
        fun onItemClick(products: Product)//service : Service de alabilir.
    }

    class PlaceHolder(val binding: EachProductBinding) : RecyclerView.ViewHolder(binding.root) {
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaceHolder {// layout ile bağlama işlemi, view binding ile
        val binding = DataBindingUtil.inflate<EachProductBinding>(LayoutInflater.from(parent.context), R.layout.each_product,parent,false)
        return PlaceHolder(binding)
    }
    override fun onBindViewHolder(
        holder: PlaceHolder,
        position: Int
    ) {
        holder.binding.product = products[position]
        holder.binding.imageOfProduct.downloadFromUrl(
            products[position].thumbnail,
            placeholderProgressBar(holder.itemView.context)
        )
        //holder.binding.textViewProductName.text = products[position].title.toString()
        // holder.binding.textViewProductPrice.text = "$".also {products[position].price.toString() }
        cartDatabase = CartDatabase.invoke(context)
        //Picasso.with(context).load(products[position].thumbnail).into(holder.binding.imageOfProduct)
        holder.binding.buttonAddToCart.setOnClickListener {
            CoroutineScope(Dispatchers.IO).
                launch { // Todo(if there is a thread switch to coroutine)
                    if (cartDatabase?.cartDao()
                            ?.searchForEntity((holder.absoluteAdapterPosition.plus(1))) != products[position].id
                        || cartDatabase?.cartDao()?.rowCount() == 0
                    ) {
                        //INSERT
                        println("INSERT")
                        cartDatabase?.cartDao()?.insertEntity(
                            Cart(
                                products[position].id,
                                products[position].title,
                                products[position].discountPercentage,
                                products[position].description,
                                products[position].price,
                                products[position].rating,
                                products[position].stock,
                                products[position].brand,
                                products[position].thumbnail,
                                true,
                                1
                            )
                        )
                        println(cartDatabase?.cartDao()?.getAllEntities())
                    }
                    else {
                        //DELETE
                        println("DELETE")
                        cartDatabase?.cartDao()?.delete(holder.absoluteAdapterPosition.plus(1))
                        println(cartDatabase?.cartDao()?.getAllEntities())
                    }
            }

        }
        holder.itemView.setOnClickListener { // holder.binding.cardView.setOnClickListener
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra("product", products[position])
            context.startActivity(intent)
            //listener.onItemClick(products[position])
        }
        favoriteDatabase = FavoriteDatabase.invoke(context) //TODO(make in a coroutine)
        val database = favoriteDatabase?.favoriteDao()
        holder.binding.checkBox.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch{
                var images = ""
                products[position].images?.forEach {
                    images += "$it "
                }
                if(holder.binding.checkBox.isChecked){
                    Utils.vibrateDevice(context)
                    favorite = Favorite( //TODO(is there better structure)
                            products[position].id,
                            products[position].title,
                            products[position].description,
                            products[position].price,
                            products[position].rating,
                            products[position].stock,
                            products[position].discountPercentage,
                            products[position].category,
                            products[position].brand,
                            products[position].thumbnail,
                            images,
                            true
                    )
                    database?.insertEntity(favorite!!)
                }else{
                    database?.delete(holder.absoluteAdapterPosition.plus(1))
                }
            }
        }
        /*runBlocking {
            GlobalScope.launch {

                if (
                    database?.searchForEntity((holder.absoluteAdapterPosition.plus(1))) != products[position].id
                    || favoriteDatabase?.favoriteDao()?.rowCount() == 0
                ) {
                    Utils.vibrateDevice(context)
                    println("\nINSERT")
                    favorite = Favorite( //TODO(is there better structure)
                        products[position].id,
                        products[position].title,
                        products[position].description,
                        products[position].price,
                        products[position].rating,
                        products[position].thumbnail,
                        0
                    )
                    favoriteDatabase?.favoriteDao()?.insertEntity(favorite!!)
                } else {
                    println("\nDELETE")
                    favoriteDatabase?.favoriteDao()?.delete(holder.absoluteAdapterPosition.plus(1))
                }
            }
        }*/
                /*if (database?.searchForEntity((holder.absoluteAdapterPosition.plus(1))) == holder.absoluteAdapterPosition.plus(1)){
                (context as Activity).runOnUiThread {
                    holder.binding.checkBox.isChecked = true
                }
            }*/
                    /*   if (products[position].id?.let {
                               favoriteDatabase?.favoriteDao()?.searchForEntity(it)
                           } == products[position].id) {
                           favoriteDatabase?.favoriteDao()?.delete(holder.absoluteAdapterPosition+1)
                           println(favoriteDatabase?.favoriteDao()?.getAllEntities())
                       }else
                       {
                           println(favoriteDatabase?.favoriteDao()?.getAllEntities())
                           products[position].id?.let { it1 -> insertItem(it1) }
                       }*/

    }
    override fun getItemCount(): Int {
        return products.count()
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getItemViewType(position: Int): Int {
        return position
    }
    override fun onViewAttachedToWindow(holder: PlaceHolder) {
        super.onViewAttachedToWindow(holder)
            CoroutineScope(Dispatchers.IO).launch {
                if (favoriteDatabase?.favoriteDao()
                        ?.searchForEntity((holder.absoluteAdapterPosition.plus(1))) == holder.absoluteAdapterPosition.plus(
                        1
                    )
                ) {
                    (context as Activity).runOnUiThread {
                        holder.binding.checkBox.isChecked = true
                    }
                }
            }
    }
    override fun onViewRecycled(holder: PlaceHolder) {
        super.onViewRecycled(holder)

        //holder.binding.checkBox.isChecked = false // - this line do the trick
    }
}