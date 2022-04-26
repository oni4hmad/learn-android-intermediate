package com.dicoding.picodiploma.storyapp1.ui.register

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.dicoding.picodiploma.storyapp1.R
import com.dicoding.picodiploma.storyapp1.data.preferences.SessionModel
import com.dicoding.picodiploma.storyapp1.databinding.FragmentRegisterBinding

class RegisterFragment : Fragment() {

    private lateinit var _binding: FragmentRegisterBinding
    private val binding get() = _binding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(RegisterViewModel::class.java)
        viewModel.isRegisterSuccess.observe(viewLifecycleOwner) {
            if (it) toLogin()
        }
        viewModel.toastText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { toastText ->
                showToast(toastText)
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        binding.btnRegister.setOnClickListener {
            tryRegister()
        }
    }

    private fun tryRegister() {
        val name = binding.edtName.text.toString()
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        val isNameInvalid = name.isEmpty()
        val isEmailInvalid = binding.edtEmail.isError || email.isEmpty()
        val isPasswordInvalid = binding.edtPassword.isError || password.isEmpty()
        when {
            isNameInvalid -> {
                binding.edtName.error = "Invalid name."
                binding.edtName.requestFocus()
            }
            isEmailInvalid -> binding.edtEmail.requestFocus()
            isPasswordInvalid -> binding.edtPassword.requestFocus()
            else -> viewModel.register(name, email, password)
        }
    }

    private fun toLogin() {
        val toLoginFragment = RegisterFragmentDirections.actionRegisterFragmentToLoginFragment()
        toLoginFragment.email = binding.edtEmail.text.toString()
        toLoginFragment.password = binding.edtPassword.text.toString()
        view?.findNavController()?.navigate(toLoginFragment)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.edtName.isEnabled = false
            binding.edtEmail.isEnabled = false
            binding.edtPassword.isEnabled = false
            binding.btnRegister.isEnabled = false
            binding.pbRegister.visibility = View.VISIBLE
        } else {
            binding.edtName.isEnabled = true
            binding.edtEmail.isEnabled = true
            binding.edtPassword.isEnabled = true
            binding.btnRegister.isEnabled = true
            binding.pbRegister.visibility = View.GONE
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(view?.context, text, Toast.LENGTH_SHORT).show()
    }
}