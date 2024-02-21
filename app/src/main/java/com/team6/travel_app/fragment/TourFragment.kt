package com.team6.travel_app.fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.team6.travel_app.adapter.ProductsAdapter
import com.team6.travel_app.adapter.TourAdapter
import com.team6.travel_app.data.CartDatabase
import com.team6.travel_app.data.CustomerDatabase
import com.team6.travel_app.data.Tour
import com.team6.travel_app.data.TourDatabase
import com.team6.travel_app.databinding.FragmentTourBinding
import com.team6.travel_app.model.Product
import com.team6.travel_app.utils.SwipeHelper
import com.team6.travel_app.view.ProductDetailsActivity
import com.team6.travel_app.viewmodel.TourViewModel
import kotlinx.coroutines.*


class TourFragment : Fragment() , TourAdapter.Listener{
    private lateinit var binding: FragmentTourBinding
    private lateinit var tourAdapter: TourAdapter
    private var tourList = ArrayList<Tour>()
    private var tourDatabase: TourDatabase? = null
    private lateinit var viewModel: TourViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTourBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[TourViewModel::class.java] //tạo viewmodel của lớp favor vm
        // đang bị lỗi
        viewModel.getData(requireContext())
        tourDatabase = context?.let { TourDatabase.invoke(it) } // tạo một đối tượng favor db
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getDataFromRoom(requireContext())

//        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.favoriteRecyclerView) {
//            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
//                val buttons = ArrayList<UnderlayButton>()
//                val deleteButton = deleteButton(position)
//
//                runBlocking {
//                    Log.i(TAG, "instantiateUnderlayButton: inside runblocking")
//                    CoroutineScope(Dispatchers.IO).launch {
//                        if(position != -1){
//
//                        }
//                        val isAddedToCart = tourAdapter?.getItemInfo(position)?.let {
//                            CartDatabase.invoke(context = requireContext()).cartDao().searchEntity(it)
//                        }
//
//                        Log.i(TAG, "instantiateUnderlayButton: the position is $position")
//
//                        Log.i(TAG, "instantiateUnderlayButton: $isAddedToCart")
//                        buttons.add(deleteButton)
//
//                        Log.i(TAG, "coroutine scope: $isAddedToCart ")
//                    }
//                }
//                return buttons
//            }
//        })
//
////        viewModel.tours.observe(viewLifecycleOwner, androidx.lifecycle.Observer { tours ->
////            tours.let {
////                tourAdapter = tours?.let { it1 ->
////                    TourAdapter(
////                        it1 as ArrayList<Tour>,
////                        requireContext() // TODO Fragment HomeFragment not attached to a context.
////                    )
////                }!!
////                binding.favoriteRecyclerView.adapter = tourAdapter
////            }
////
////        })
//
//        itemTouchHelper.attachToRecyclerView(binding.favoriteRecyclerView)
        liveDataObserver()
    }

    private fun initializeRecyclerView(tours: List<Tour>) {
        val layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.favoriteRecyclerView.layoutManager = layoutManager
        tourAdapter = TourAdapter(tours as ArrayList<Tour>, requireContext())
        binding.favoriteRecyclerView.adapter = tourAdapter
    }

    private fun liveDataObserver() {
        viewModel.isListEmpty.observe(viewLifecycleOwner, Observer { isEmpty ->
            if (isEmpty) {
                binding.emptyList.visibility = View.VISIBLE
            } else {
                binding.emptyList.visibility = View.INVISIBLE
            }
        })
        viewModel.tours2.observe(viewLifecycleOwner, Observer {
            binding.progressBar.visibility = View.INVISIBLE
            if (it.isEmpty()) {
                binding.emptyList.visibility = View.VISIBLE
            } else {
                initializeRecyclerView(it)
                binding.emptyList.visibility = View.INVISIBLE
            }
        })
        viewModel.product.observe(viewLifecycleOwner, Observer {
            it.let {

            }
        })
    }

    //hàm xoá khỏi favor
    private fun deleteButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            "Huỷ chuyến",
            14.0f,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    tourDatabase?.let {
                        tourAdapter?.getItemInfo(position)
                            ?.let { it1 -> viewModel.removeItemFromRoom(it1, requireContext()) }
                    }
                    tourAdapter?.deleteItem(position)
                }
            })
    }

    // hàm thêm giỏ hàng, nên bỏ
//    private fun addToCartButton(position: Int): SwipeHelper.UnderlayButton {
//        return SwipeHelper.UnderlayButton(
//            requireContext(),
//            "Add to Cart",
//            14.0f,
//            android.R.color.holo_blue_light,
//            object : SwipeHelper.UnderlayButtonClickListener {
//                override fun onClick() {
//                    val id = tourAdapter!!.getItemInfo(position)
//                        if (id != null) {
//                            viewModel.addToCart(requireContext(), id = id)
//                        }
//                }
//            })
//    }

    // mở thông tin từng tour, nghiên cứu sau
    override fun onItemClick(products: Product) {
        tourList.forEach {
            if(it.id == products.id){
                var item = it.id
            }
        }
        val intent = Intent(context, ProductDetailsActivity::class.java)
        intent.putExtra("product", tourList)
        context?.startActivity(intent)
    }
}