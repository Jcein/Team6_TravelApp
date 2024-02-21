package com.team6.travel_app.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.team6.travel_app.data.CustomerDatabase
import com.team6.travel_app.databinding.FragmentLogInBinding
import com.team6.travel_app.view.MainActivity
import com.team6.travel_app.viewmodel.CustomerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class LogInFragment : Fragment() {
    private lateinit var binding : FragmentLogInBinding
    private lateinit var viewModel: CustomerViewModel
    private lateinit var customerDatabase: CustomerDatabase



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLogInBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CustomerViewModel::class.java]

        binding.buttonSignInAnonymously.setOnClickListener {
            val account = binding.etEmail.text?.toString().takeIf { !it.isNullOrBlank() } ?: ""
            val password = binding.etPassword.text?.toString().takeIf { !it.isNullOrBlank() } ?: ""

            viewModel.getData(requireContext(), account, password)
            CoroutineScope(Dispatchers.IO).launch {

                if (viewModel.rowCount != 0) {
                    val intent = Intent(activity, MainActivity::class.java)
                    startActivity(intent)
                    activity?.finish()
                } else {
                    print("not ok")
                }
            }
        }
    }
}