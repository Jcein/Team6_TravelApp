package com.team6.travel_app.fragment

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.team6.travel_app.adapter.CartAdapter
import com.team6.travel_app.data.CartDatabase
import com.team6.travel_app.databinding.FragmentCartBinding
import com.team6.travel_app.utils.SwipeHelper
import com.team6.travel_app.view.ProductDetailsActivity
import com.team6.travel_app.viewmodel.CartViewModel
import com.stripe.android.PaymentConfiguration

import com.team6.travel_app.R

import com.team6.travel_app.fragment.*


import kotlinx.coroutines.*


class CartFragment : Fragment() {
    private lateinit var viewModel: CartViewModel
    private lateinit var cartAdapter: CartAdapter
    lateinit var binding: FragmentCartBinding
    private lateinit var cartDatabase: CartDatabase
    //private var cartFragment = CartFragment()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCartBinding.inflate(layoutInflater)
        //viewModel = ViewModelProvider(this)[CartViewModel::class.java]

        return binding.root
    }



    // mất hết dữ liệu, cả sản phẩm và total
    // phương thức này gọi 2 hàm viewModelObserver(), setUpRecyclerView()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // thanh toán
        PaymentConfiguration.init(
            requireActivity(),
            "pk_test_qblFNYngBkEdjEZ16jxxoWSM"
        )
        // binding.recyclerViewCart.layoutManager = LinearLayoutManager(context,RecyclerView.VERTICAL,false)
        viewModel = ViewModelProvider(this)[CartViewModel::class.java]

        cartDatabase = CartDatabase.invoke(requireContext())
        viewModel.getDataFromRoom(requireContext(), binding, cartDatabase)
        /* if(viewModel.cartList.value?.isEmpty() == true){
             binding.emptyListMessage.visibility = View.VISIBLE
         }*/

        binding.progressBar.visibility = View.GONE


        CoroutineScope(Dispatchers.IO).launch {
            if (cartDatabase.cartDao().rowCount() == 0) {
                withContext(Dispatchers.Main) {
                    binding.emptyListMessage.visibility = View.VISIBLE
                }

            } else {
                withContext(Dispatchers.Main) {
                    binding.emptyListMessage.visibility = View.INVISIBLE
                }
            }
        }

        viewModelObserver()
        setUpRecyclerView()


    }

    //nếu comment sẽ mất giá (total)
    //hàm này là hàm được gọi
    private fun viewModelObserver() {
        viewModel.apply {
            //tinh total
            calculateTotalAmount(cartDatabase)

            isCartListLoading.observe(viewLifecycleOwner, Observer {
            })
            totalAmounth.observe(viewLifecycleOwner, Observer {
                binding.totalAmount.text = it.toString() + " đ"
                println("total amount inside observe : ${it}")
                println("text : ${binding.totalAmount.text.toString()} ")
            })
            isCartListEmpty.observe(viewLifecycleOwner, Observer {
                if (it) {
                    binding.emptyListMessage.visibility = View.VISIBLE
                } else {
                    binding.emptyListMessage.visibility = View.INVISIBLE
                }
            })
            product.observeForever(Observer {
                Log.i(TAG, "viewModelObserver: inside of observer")
                it.let {
                    Log.i(TAG, "viewModelObserver: $it")
                    val intent = Intent(context, ProductDetailsActivity::class.java)
                    intent.putExtra("product", it)
                    context?.startActivity(intent)
                }
            })

        }
    }

    // nếu comment sẽ mất sản phẩm trong card
    //hàm này là hàm được gọi
    private fun setUpRecyclerView() {

        cartAdapter = viewModel.cartList.value?.let {
            CartAdapter(it, requireContext(), cartDatabase, binding)
        }!!

        binding.recyclerViewCart.adapter = cartAdapter




        binding.recyclerViewCart.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        binding.recyclerViewCart.layoutManager = LinearLayoutManager(requireContext())



        val itemTouchHelper = ItemTouchHelper(object : SwipeHelper(binding.recyclerViewCart) {
            override fun instantiateUnderlayButton(position: Int): List<UnderlayButton> {
                val buttons = ArrayList<UnderlayButton>()
                val deleteButton = deleteButton(position)
                buttons.add(deleteButton)
                return buttons
            }
        })

        itemTouchHelper.attachToRecyclerView(binding.recyclerViewCart)

        binding.buttonPurchase.setOnClickListener(View.OnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                // Gọi hàm postRequest ở đây
                if (cartDatabase.cartDao().rowCount() == 0) {
                    print("bạn chưa tham gia tour nào")
                    //Toast.makeText(context,"bạn chưa tham gia tour nào",Toast.LENGTH_SHORT).show()

                } else {
                    viewModel.postRequest(requireContext(), cartDatabase)
                    viewModel.viewModelScope.launch {
                        val cardDb = CartDatabase.invoke(requireContext()).cartDao()
                        cardDb.deleteAllRecords()
                    }
//                    beginTransaction(CartFragment())
                }
            }


        })
    }

//    private fun beginTransaction(fragment: Fragment) {
//        childFragmentManager.beginTransaction().apply {
//            replace(R.id.cartFrameLayout, fragment)
//            commit()
//        }
//    }
    //Nút loại bỏ
    private fun deleteButton(position: Int): SwipeHelper.UnderlayButton {
        return SwipeHelper.UnderlayButton(
            requireContext(),
            "Loại Bỏ",
            14.0f,
            android.R.color.holo_red_light,
            object : SwipeHelper.UnderlayButtonClickListener {
                override fun onClick() {
                    viewModel.removeItemFromRoom(cartAdapter.getItemInfo(position)!!, cartDatabase)
                    cartAdapter.deleteItem(position)
                }
            })
    }

    fun purchase() {
//        viewModel.postRequest(requireContext())
    }


}