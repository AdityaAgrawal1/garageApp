package com.example.garageapp.auth.ui

import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import androidx.annotation.RequiresApi
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.garageapp.R
import com.example.garageapp.auth.data.AuthViewModel
import com.example.garageapp.base.BaseFragment
import com.example.garageapp.databinding.FragmentSignupBinding
import com.example.garageapp.main.db.DbResource
import com.example.garageapp.utils.UserLoginPreferences
import com.example.garageapp.utils.printDebug
import com.example.garageapp.utils.snackBar
import com.example.garageapp.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@AndroidEntryPoint
class SignupFragment : BaseFragment<FragmentSignupBinding, AuthViewModel>(){

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
                        signupDataChanged(
                            it.userName.text.toString(),
                            it.password.text.toString(),
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


    @RequiresApi(Build.VERSION_CODES.O)
    override fun setUpViews() {

        binding?.apply {
            label = "SignUp"

            password.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    signup()
                }
                false
            }

            signupButton.setOnClickListener {
                signup()
            }

            alternateSignIn.setOnClickListener{
                findNavController().navigate(
                    R.id.action_signupFragment_to_loginFragment,
                    null,
                    navOptions)
            }
        }
    }

    override fun observeData() {
        viewModel?.apply {
            signupFormState.observe(viewLifecycleOwner){ formState ->
                if (formState == null) {
                    return@observe
                }
                binding?.apply {
                    signupButton.isEnabled = formState.isDataValid
                    usernameTil.error = formState.userNameError?.let { getString(it) }
                    passwordTil.error = formState.passwordError?.let { getString(it) }
                }
            }
            insertUsersDataStatus.observe(viewLifecycleOwner) {
                binding?.loading?.visible(it is DbResource.Loading)
                when (it) {
                    is DbResource.Success -> {
                        findNavController().navigate(R.id.action_signupFragment_to_carFragment,
                            null,
                            navOptions
                        )
                        runBlocking {
                            UserLoginPreferences(requireContext()).saveUserId(it.value)
                        }
                    }
                    is DbResource.Failure -> {
                        printDebug("it.message = ${it.errorMsg}")
                        requireView().snackBar(it.errorMsg.toString())
                    }
                    else -> {}
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun signup() {
        val userName = binding?.userName?.text.toString()
        val password = binding?.password?.text.toString()
        viewModel?.signup(userName, password)
    }


    override fun initViewModel(): AuthViewModel = authViewModel


    override fun getViewBinding(): FragmentSignupBinding =
        FragmentSignupBinding.inflate(layoutInflater)

}