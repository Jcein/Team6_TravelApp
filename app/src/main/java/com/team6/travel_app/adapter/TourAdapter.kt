package com.team6.travel_app.adapter


import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.team6.travel_app.R
import com.team6.travel_app.data.Tour
import com.team6.travel_app.data.ImageDatabase
import com.team6.travel_app.data.ProductDatabase
import com.team6.travel_app.data.TourDatabase
import com.team6.travel_app.databinding.EachTourBinding
import com.team6.travel_app.model.Product
import com.team6.travel_app.utils.Utils
import com.team6.travel_app.utils.downloadFromUrl
import com.team6.travel_app.view.ProductDetailsActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TourAdapter(
    private val tourList: ArrayList<Tour>,
    val context: Context
) : RecyclerView.Adapter<TourAdapter.PlaceHolder>() {
    interface Listener {
        fun onItemClick(products: Product)//service : Service de alabilir.
    }
    class PlaceHolder(val binding: EachTourBinding) : RecyclerView.ViewHolder(binding.root) {
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PlaceHolder {
        val binding = DataBindingUtil.inflate<EachTourBinding>(LayoutInflater.from(parent.context),
                R.layout.each_tour, parent, false)
        return PlaceHolder(binding)
    }

    override fun onBindViewHolder(
        holder: PlaceHolder,
        position: Int
    ) {
        // hiển thị hình ảnh
        holder.binding.imageOfProduct.downloadFromUrl(tourList[position].thumbnail,
            CircularProgressDrawable(context)
        )
        //hiện thị thông tin tour
        holder.binding.eachTour = tourList[position]

        // ấn vào để xem thông tin tour
//        holder.itemView.setOnClickListener {// TODO migrate to data binding and MVVM
//                val id = tourList[position].id
//                CoroutineScope(Dispatchers.IO).launch() {
//                    val images = ImageDatabase(context = context).imageDao().getRecord(id!!)
//                    val item = ProductDatabase(context).productDao().getRecord(id)
//                    val product = Product(item.id,item.title,item.description,item.price,item.discountPercentage,item.rating,item.stock,item.brand,item.category,item.thumbnail,images)
//                    val intent = Intent(context, ProductDetailsActivity::class.java)
//                    intent.putExtra("product", product)
//                    context.startActivity(intent)
//                }
//
//               // Log.i(ContentValues.TAG, "onBindViewHolder: ${tourList[position].id}")
//                //val viewModel = CartViewModel()
//                //viewModel.onViewClicked(context, tourList[position].id!!) // TODO viewmodel isn't triggered
//        }

        //val double = tourList[position].rating
        //val dolarSign = "$"
        //val doubleWithLastTwoDigits = ((double?.times(10.0))?.roundToInt() ?: 0) / 10.0
        //holder.binding.textViewProductName.text = tourList[position].title
        //holder.binding.textViewProductDescription.text = tourList[position].description
        //(dolarSign + tourList[position].price.toString()).also { holder.binding.textViewProductPrice.text = it }
       // holder.binding.textViewRatingCount.text = doubleWithLastTwoDigits.toString()
}
    override fun getItemCount(): Int {
        println("getItemCount"
                +tourList.size)
        return tourList.size
    }
    fun deleteItem(position: Int) {
        tourList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, tourList.size)
    }
    fun getItemInfo(position: Int): Int? {
        return tourList[position].id
    }

}