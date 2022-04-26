package com.dicoding.picodiploma.storyapp1.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.dicoding.picodiploma.storyapp1.R
import com.dicoding.picodiploma.storyapp1.data.preferences.SessionModel
import com.dicoding.picodiploma.storyapp1.data.preferences.SessionPreference
import com.dicoding.picodiploma.storyapp1.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var mSessionPreference: SessionPreference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
        viewModel.session.observe(viewLifecycleOwner) {
            login(it)
        }
        viewModel.toastText.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { toastText ->
                showToast(toastText)
            }
        }
        viewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        mSessionPreference = SessionPreference(view.context)
        if (mSessionPreference.getSession() != null) {
            view.findNavController().navigate(R.id.action_loginFragment_to_listStoryActivity)
            activity?.finish()
        }

        LoginFragmentArgs.fromBundle(arguments as Bundle).email?.let {
            binding.edtEmail.setText(it)
        }
        LoginFragmentArgs.fromBundle(arguments as Bundle).password?.let {
            binding.edtPassword.setText(it)
        }

        binding.btnLogin.setOnClickListener {
            tryLogin()
        }

        binding.tvSignUp.setOnClickListener (
            Navigation.createNavigateOnClickListener(R.id.action_loginFragment_to_registerFragment)
        )
    }

    private fun tryLogin() {
        val email = binding.edtEmail.text.toString()
        val password = binding.edtPassword.text.toString()
        val isEmailInvalid = binding.edtEmail.isError || email.isEmpty()
        val isPasswordInvalid = binding.edtPassword.isError || password.isEmpty()
        when {
            isEmailInvalid -> binding.edtEmail.requestFocus()
            isPasswordInvalid -> binding.edtPassword.requestFocus()
            else -> viewModel.login(email, password)
        }
    }

    private fun login(session: SessionModel) {
        mSessionPreference.setSession(session)
        view?.findNavController()?.navigate(R.id.action_loginFragment_to_listStoryActivity)
        activity?.finish()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.edtEmail.isEnabled = false
            binding.edtPassword.isEnabled = false
            binding.btnLogin.isEnabled = false
            binding.tvSignUp.isEnabled = false
            binding.pbLogin.visibility = View.VISIBLE
        } else {
            binding.edtEmail.isEnabled = true
            binding.edtPassword.isEnabled = true
            binding.btnLogin.isEnabled = true
            binding.tvSignUp.isEnabled = true
            binding.pbLogin.visibility = View.GONE
        }
    }

    private fun showToast(text: String) {
        Toast.makeText(view?.context, text, Toast.LENGTH_SHORT).show()
    }
}