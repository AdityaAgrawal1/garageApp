package com.example.garageapp.cars.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.garageapp.R
import com.example.garageapp.base.BaseFragment
import com.example.garageapp.cars.adapter.CarRecyclerViewAdapter
import com.example.garageapp.cars.data.CarViewModel
import com.example.garageapp.databinding.FragmentCarBinding
import com.example.garageapp.main.db.entities.Car
import com.example.garageapp.main.db.resources.DbResource
import com.example.garageapp.networks.Resource
import com.example.garageapp.utils.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class CarFragment : BaseFragment<FragmentCarBinding, CarViewModel>(),CarRecyclerViewAdapter.ClickListener {

    private lateinit var carRecyclerViewAdapter: CarRecyclerViewAdapter

    private val carViewModel : CarViewModel by viewModels()

    private var carsList = mutableListOf<Car>()

    private var selectedCarMake = ""
    private var selectedCarModel = ""

    private var imageUri:Uri = Uri.EMPTY

    private var position = -1
    private var carId = ""

    private val carMakeClickListener =
        AdapterView.OnItemClickListener { parent, view, position, id ->
            if(position != 0){
                lifecycleScope.launch {
                    val makeId = (carViewModel.carMakeResponse.value as Resource.Success).value
                        .results
                        ?.find{ it?.makeName == binding?.carMake?.adapter?.getItem(position).toString() }
                        ?.makeID
                    carModels.clear()
                    binding?.carModel?.clearListSelection()
                    binding?.carModel?.setText("Select Car Model", false)
                    selectedCarMake = binding?.carMake?.adapter?.getItem(position).toString()
                    carViewModel.getCarModels(makeId!!)
                }
            }
        }

    private val carModelClickListener =
        AdapterView.OnItemClickListener { parent, view, position, id ->
            if(position != 0){
                lifecycleScope.launch {
                    selectedCarModel = binding?.carModel?.adapter?.getItem(position).toString()
                    if(checkStateSoftKeyBoard()){
                        hideKeyboard()
                    }
                }
            }
        }

    private var carMakes = mutableListOf<String>()
    private var carModels = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun setUpViews() {
        carViewModel.getCarMakes()

        lifecycleScope.launch {
            carViewModel.getAddedCars(UserLoginPreferences(requireContext()).userId.first()?:0)
        }

        carRecyclerViewAdapter = CarRecyclerViewAdapter(requireContext(), carsList,this)

        binding?.apply {
            label = "Dashboard"
            rvCars.adapter = carRecyclerViewAdapter
        }
        binding?.header?.root?.findViewById<AppCompatButton>(R.id.btn_logout)?.setOnClickListener {
            lifecycleScope.launch {
                carViewModel.logout()
                findNavController().navigate(R.id.loginFragment)
                shortToast("Logout Successfully")
            }
        }

        binding?.apply {
            carMake.onItemClickListener = carMakeClickListener
            carModel.onItemClickListener = carModelClickListener
            carModel.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    listOf("Select Car Model")
                )
            )
            carMake.setSelection(0)
            carModel.setSelection(0)
        }


        binding?.addCar?.setOnClickListener {
            if(checkStateSoftKeyBoard()){
                hideKeyboard()
            }
            addCar()
        }

    }




    override fun observeData() {
        carViewModel.apply {
            carMakeResponse.observe(viewLifecycleOwner){
                binding?.loading?.visible(it is Resource.Loading)
                when (it) {
                    is Resource.Success -> {
                        carMakes = mutableListOf("Select Car Make")
                        carMakes.addAll(it.value.results?.map { make -> make?.makeName!! } ?: listOf())

                        binding?.carMake?.setAdapter(ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            carMakes
                        ))
                        binding?.apply {
                            carMake.hint = "Select Car Make"
                            carModel.hint = "Select Car Model"
                        }
                    }

                    is Resource.Failure -> handleApiError(it) { }

                    else -> Unit
                }
            }
            carModelResponse.observe(viewLifecycleOwner){
                binding?.loading?.visible(it is Resource.Loading)
                when (it) {
                    is Resource.Success -> {
                        carModels.clear()
                        binding?.carModel?.clearListSelection()
                        carModels = mutableListOf("Select Car Model")
                        carModels.addAll(it.value.results
                            ?.map { model -> model?.modelName !! }
                            ?.toTypedArray()
                            ?: arrayOf()
                        )
                        binding?.carModel?.setAdapter(ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_list_item_1,
                            carModels
                        ))
                        binding?.carModel?.setSelection(0)
                    }

                    is Resource.Failure -> handleApiError(it) { }

                    else -> Unit
                }
            }

            carInsertDataStatus.observe(viewLifecycleOwner){
                binding?.loading?.visible(it is DbResource.Loading)
                when(it){
                    is DbResource.Success ->{
                        lifecycleScope.launch {
                            getAddedCars(UserLoginPreferences(requireContext()).userId.first()?:0)
                        }
                    }
                    is DbResource.Failure -> {
                        printDebug("it.message = ${it.errorMsg}")
                        requireView().snackBar(it.errorMsg.toString())
                    }
                    else -> {}
                }
            }
            carsData.observe(viewLifecycleOwner){
                binding?.loading?.visible(it is DbResource.Loading)
                when(it){
                    is DbResource.Success ->{
                        binding?.apply {
                            carMake.setText("Select Car Make", false)
                            carModel.setText("Select Car Model", false)
                            carModels.clear()
                        }
                        carsList = it.value.toMutableList()
                        carRecyclerViewAdapter.updateCarsList(it.value)
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
    private fun addCar(){
        lifecycleScope.launch {
            val userId = UserLoginPreferences(requireContext()).userId.first()?:0
            carViewModel.addCar(selectedCarMake, userId, selectedCarModel)
        }
    }


    private fun requestStoragePermission() {
        if (
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                STORAGE_PERMISSION_REQ_CODE
            )
        }else{
            showUpdatePhotoDialog()
        }
    }

    private fun requestCameraPermission() {
        if (
            ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQ_CODE
            )
        }else{
            val takePicture = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(takePicture, 0)
        }
    }

    private fun showUpdatePhotoDialog(){
        val options = resources.getStringArray(R.array.car_image_options)

        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.update_car_image_dialog_title)
        builder.setItems(options) { dialog, which ->
            when(which){
                0 -> {
                    requestCameraPermission()
                }
                1 -> {
                    val gallery = Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                    startActivityForResult(gallery, 1)
                }
                2 -> dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun getImageUri(inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(
            context?.contentResolver,
            inImage,
            carId,
            null
        )
        return Uri.parse(path)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode != Activity.RESULT_CANCELED) {
            when (requestCode) {
                0 -> if (resultCode == Activity.RESULT_OK && data != null) {
                    val img = data.extras!!["data"] as Bitmap
                    val imgPath: String? =
                        FileUtil.getRealPathFromURI(getImageUri(img), context?.contentResolver!! )

                    imageUri = Uri.parse(imgPath)?: Uri.EMPTY
                    carRecyclerViewAdapter.updateCarImage(position,imageUri)
                    lifecycleScope.launch {
                        carViewModel.updateCarImage(carId,imageUri.toString())
                    }
                }
                1 -> if (resultCode == Activity.RESULT_OK && data != null) {
                    imageUri = data.data?: Uri.EMPTY
                    carRecyclerViewAdapter.updateCarImage(position,imageUri)
                    lifecycleScope.launch {
                        carViewModel.updateCarImage(carId,imageUri.toString())
                    }
                }
            }
        }
    }


    override fun initViewModel(): CarViewModel = carViewModel


    override fun getViewBinding(): FragmentCarBinding =
        FragmentCarBinding.inflate(layoutInflater)


    override fun onClickAddCarImage(position: Int) {
        this.position = position
        carId = carsList[position].id
        requestStoragePermission()
    }

    override fun onClickDeleteCar(position: Int) {
        val carId = carsList[position].id
        lifecycleScope.launch {
            carsList.remove(carsList.find { it.id==carId })
            carViewModel.removeCar(carId)
        }
    }

    companion object{
        private const val CAMERA_PERMISSION_REQ_CODE = 124
        private const val STORAGE_PERMISSION_REQ_CODE = 123
    }
}