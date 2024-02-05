package com.team6.travel_app.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.team6.travel_app.R
import com.team6.travel_app.data.Favorite
import com.team6.travel_app.data.ImageDatabase
import com.team6.travel_app.data.ProductDatabase
import com.team6.travel_app.databinding.EachFavoriteBinding
import com.team6.travel_app.model.Product
import com.team6.travel_app.utils.downloadFromUrl
import com.team6.travel_app.view.ProductDetailsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteAdapter(
    private val favoriteList: ArrayList<Favorite>,
    val context: Context
) : RecyclerView.Adapter<FavoriteAdapter.PlaceHolder>() {
    interface Listener {
        fun onItemClick(products: Product)//service : Service de alabilir.
    }
    class PlaceHolder(val binding: EachFavoriteBinding) : RecyclerView.ViewHolder(binding.root) {
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaceHolder {
        val binding = DataBindingUtil.inflate<EachFavoriteBinding>(LayoutInflater.from(parent.context),
                R.layout.each_favorite, parent, false)
        return PlaceHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PlaceHolder,
        position: Int
    ) {
        holder.binding.imageOfProduct.downloadFromUrl(favoriteList[position].thumbnail,
            CircularProgressDrawable(context)
        )
        holder.binding.eachFavorite = favoriteList[position]
        holder.itemView.setOnClickListener {// TODO migrate to data binding and MVVM
                val id = favoriteList[position].id
                CoroutineScope(Dispatchers.IO).launch() {
                    val images = ImageDatabase(context = context).imageDao().getRecord(id!!)
                    val item = ProductDatabase(context).productDao().getRecord(id)
                    val product = Product(item.id,item.title,item.description,item.price,item.discountPercentage,item.rating,item.stock,item.brand,item.category,item.thumbnail,images)
                    val intent = Intent(context, ProductDetailsActivity::class.java)
                    intent.putExtra("product", product)
                    context.startActivity(intent)
                }

               // Log.i(ContentValues.TAG, "onBindViewHolder: ${favoriteList[position].id}")
                //val viewModel = CartViewModel()
                //viewModel.onViewClicked(context, favoriteList[position].id!!) // TODO viewmodel isn't triggered
        }
        //val double = favoriteList[position].rating
        //val dolarSign = "$"
        //val doubleWithLastTwoDigits = ((double?.times(10.0))?.roundToInt() ?: 0) / 10.0
        //holder.binding.textViewProductName.text = favoriteList[position].title
        //holder.binding.textViewProductDescription.text = favoriteList[position].description
        //(dolarSign + favoriteList[position].price.toString()).also { holder.binding.textViewProductPrice.text = it }
       // holder.binding.textViewRatingCount.text = doubleWithLastTwoDigits.toString()
}
    override fun getItemCount(): Int {
        println("getItemCount"
                +favoriteList.size)
        return favoriteList.size
    }
    fun deleteItem(position: Int) {
        favoriteList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, favoriteList.size)
    }
    fun getItemInfo(position: Int): Int? {
        return favoriteList[position].id
    }

}