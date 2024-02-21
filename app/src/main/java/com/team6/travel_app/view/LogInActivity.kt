package com.team6.travel_app.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.team6.travel_app.R
import com.team6.travel_app.adapter.TabLayoutAdapter
import com.team6.travel_app.databinding.ActivityLogInBinding
import com.team6.travel_app.fragment.LogInFragment
import com.team6.travel_app.fragment.SignUpFragment

class LogInActivity : AppCompatActivity() {
    lateinit var binding: ActivityLogInBinding

    companion object {
        const val CUS_URL = "http://nguyenvantuanolivas.mooo.com/api2/"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_Ecommerceapp_NoActionBar)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setUpTabs()
    }

    private fun setUpTabs() {
        val adapter = TabLayoutAdapter(supportFragmentManager)
        adapter.addFragment(LogInFragment(),"Đăng nhập")
        adapter.addFragment(SignUpFragment(),"Đăng ký")
        binding.viewPager.adapter = adapter
        binding.tabLayout.setupWithViewPager(binding.viewPager)
        /*  tabLayout.getTabAt(0)?.setIcon(R.drawable.vector_card)
          tabLayout.getTabAt(1)?.setIcon(R.drawable.vector_card)
  */
    }

}