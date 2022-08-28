package com.example.garageapp.auth.ui

import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.garageapp.R
import com.example.garageapp.auth.data.AuthViewModel
import com.example.garageapp.base.BaseFragment
import com.example.garageapp.databinding.FragmentLoginBinding
import com.example.garageapp.main.db.DbResource
import com.example.garageapp.utils.UserLoginPreferences
import com.example.garageapp.utils.printDebug
import com.example.garageapp.utils.snackBar
import com.example.garageapp.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class LoginFragment : BaseFragment<FragmentLoginBinding, AuthViewModel>() {

    private val authViewModel : AuthViewModel by viewModels()

    private val afterTextChangedListener = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        }

        override fun afterTextChanged(s: Editable) {
            viewModel?.apply {
                binding?.let {
                    lifecycleScope.launch {
                        loginDataChanged(
                            it.userName.text.toString().trim(),
                            it.password.text.toString()
                        )
                    }
                }
            }
        }
    }


    override fun onResume() {
        super.onResume()
        binding?.apply {
            userName.addTextChangedListener(afterTextChangedListener)
            password.addTextChangedListener(afterTextChangedListener)
        }
    }


    override fun onPause() {
        super.onPause()
        binding?.apply {
            userName.removeTextChangedListener(afterTextChangedListener)
            password.removeTextChangedListener(afterTextChangedListener)
        }
    }


    override fun setUpViews() {

        binding?.apply {
            label = "Login"

            password.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    login()
                }
                false
            }


            login.setOnClickListener {
                viewModel?.apply {
                    login()
                }
            }


            alternateSignUp.setOnClickListener{
                findNavController().navigate(
                    R.id.action_loginFragment_to_signupFragment,
                    null,
                    navOptions
                )
            }

        }
    }


    override fun observeData() {
        viewModel?.apply {
            loginFormState.observe(viewLifecycleOwner){ formState ->
                if (formState == null) {
                    return@observe
                }
                binding?.apply {
                    login.isEnabled = formState.isDataValid
                    usernameTil.error = formState.userNameError?.let { getString(it) }
                    passwordTil.error = formState.passwordError?.let { getString(it) }
                }
            }

            userLoginDataStatus.observe(viewLifecycleOwner){
                binding?.loading?.visible(it is DbResource.Loading)
                when (it) {
                    is DbResource.Success -> {
                        printDebug("it.messageSuccess = ${it.value.value}")
                        if (it.value.value != null) {
                            runBlocking {
                                UserLoginPreferences(requireContext()).saveUserId(it.value.value!!.id!!)
                            }
                            findNavController().navigate(R.id.action_loginFragment_to_carFragment,
                                null,
                                navOptions)
                        } else {
                            requireView().snackBar("User does not exist in database.")
                        }
                    }

                    is DbResource.Failure -> {
                        printDebug("it.message = ${it.errorMsg}")
                        requireView().snackBar(it.errorMsg.toString())
                    }

                    else -> Unit
                }
            }
        }
    }


    private fun login() {
        val userName = binding?.userName?.text.toString().trim()
        val password = binding?.password?.text.toString().trim()
        viewModel?.getUserLoginDataStatus(userName, password)
    }


    override fun initViewModel(): AuthViewModel = authViewModel


    override fun getViewBinding(): FragmentLoginBinding =
        FragmentLoginBinding.inflate(layoutInflater)

}