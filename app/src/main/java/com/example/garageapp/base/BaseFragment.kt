package com.example.garageapp.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.viewbinding.ViewBinding
import com.example.garageapp.utils.UserLoginPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 * Use the [BaseFragment] factory method to
 * create an instance of this fragment.
 */

abstract class BaseFragment<VBinding : ViewBinding, VModel : BaseViewModel> : Fragment() {
    protected var viewModel: VModel? = null
    protected abstract fun initViewModel() : VModel?

    protected var binding : VBinding? = null
    protected abstract fun getViewBinding() : VBinding

    protected lateinit var userLoginPreferences: UserLoginPreferences


    val navOptions : NavOptions by lazy {
        NavOptions.Builder().apply {
            setEnterAnim(android.R.anim.fade_in)
            setExitAnim(android.R.anim.fade_out)
            setPopEnterAnim(android.R.anim.fade_in)
            setPopExitAnim(android.R.anim.fade_out)
        }.build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        init()
        return binding?.root
    }


    private fun init(){
        viewModel = initViewModel()
        binding = getViewBinding()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        observeData()
    }


    open fun setUpViews() {}


    open fun observeData() {}

    protected fun shortToast(message : String){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }


    protected fun shortToast(resId : Int){
        Toast.makeText(context, resources.getText(resId), Toast.LENGTH_SHORT).show()
    }


    protected fun longToast(message : String){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }


    protected fun longToast(resId : Int){
        Toast.makeText(context, resources.getText(resId), Toast.LENGTH_LONG).show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}