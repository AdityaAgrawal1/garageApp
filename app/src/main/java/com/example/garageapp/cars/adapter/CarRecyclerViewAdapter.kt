package com.example.garageapp.cars.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.garageapp.R
import com.example.garageapp.cars.ui.CarDetails
import com.example.garageapp.databinding.ItemCarBinding

class CarRecyclerViewAdapter(
    private val context: Context,
    private var carsList: List<CarDetails>,
    private val onClickListener : ClickListener
    ) : RecyclerView.Adapter<CarRecyclerViewAdapter.CarItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarItemViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        return CarItemViewHolder(ItemCarBinding.inflate(layoutInflater))
    }

    override fun onBindViewHolder(holder: CarItemViewHolder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount() = carsList.size

    inner class CarItemViewHolder(private val binding: ItemCarBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(position : Int){
            binding.car = carsList[position]

            binding.carMake.text = carsList[position].carMake
            binding.carModel.text = carsList[position].carModel

            val carImageUrl = carsList[position].carImage?:""
            Glide.with(itemView.context)
                .load(carImageUrl)
                .error(
                    Glide.with(itemView.context).
                load(itemView.context?.getDrawable(R.drawable.dummy_car)))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(binding.carImage)

            binding.btnAddCarImage.setOnClickListener {
                onClickListener.onClickAddCarImage(position)
                notifyItemChanged(position)
            }
            binding.btnDeleteCar.setOnClickListener {
                onClickListener.onClickDeleteCar(position)
                notifyItemChanged(position)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateCarsList(carsList: List<CarDetails>){
        this.carsList = carsList
        notifyDataSetChanged()
    }

    fun updateCarImage(position: Int, imageUri: Uri){
        carsList[position].carImage = imageUri.toString()
        notifyItemChanged(position)
    }

    interface ClickListener{
        fun onClickAddCarImage(position : Int)
        fun onClickDeleteCar(position : Int)
    }
}