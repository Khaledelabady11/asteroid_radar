package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.AsteroidItemBinding

class MainAsteroidAdapter(val clickListener: OnClickListener) :
    ListAdapter<Asteroid, MainAsteroidAdapter.AsteroidViewHolder>(DiffUtilCallback) {

    companion object DiffUtilCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder(AsteroidItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class AsteroidViewHolder(var binding: AsteroidItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Asteroid, clickListener: OnClickListener) {
            binding.asteroid = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

    }


//    companion object {
//        fun from(parent: ViewGroup): AsteroidViewHolder {
//            val view =
//                LayoutInflater.from(parent.context).inflate(R.layout.asteroid_item, parent, false)
//
//            return AsteroidViewHolder(view)
//        }
//    }


}

class OnClickListener(val clickListener: (asteroidItem: Asteroid) -> Unit) {
    fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}