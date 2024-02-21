package com.team6.travel_app.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.team6.travel_app.R
import com.team6.travel_app.databinding.FragmentLogInBinding
import com.team6.travel_app.databinding.FragmentSignUpBinding
import com.team6.travel_app.viewmodel.CustomerViewModel


class SignUpFragment : Fragment() {
    private lateinit var binding : FragmentSignUpBinding
    private lateinit var viewModel: CustomerViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSignUpBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[CustomerViewModel::class.java]
        binding.buttonSignUp.setOnClickListener{
            val account = binding.tlAccount.text?.toString().takeIf { !it.isNullOrBlank() } ?: ""
            val pass = binding.tlPassword.text?.toString().takeIf { !it.isNullOrBlank() } ?: ""
            val name= binding.tlName.text?.toString().takeIf { !it.isNullOrBlank() } ?: ""
            val email= binding.tlEmail.text?.toString().takeIf { !it.isNullOrBlank() } ?: ""
            val phone = binding.tlPhone.text?.toString().takeIf { !it.isNullOrBlank() } ?: ""
            val address = binding.tlAddress.text?.toString().takeIf { !it.isNullOrBlank() } ?: ""

            viewModel.postRequest(requireContext(), account, pass, name, email, phone, address)
        }
    }

}