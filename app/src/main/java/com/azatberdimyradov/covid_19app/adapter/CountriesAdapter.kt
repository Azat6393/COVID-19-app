package com.azatberdimyradov.covid_19app.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.azatberdimyradov.covid_19app.R
import com.azatberdimyradov.covid_19app.databinding.RecyclerViewCountriesItemBinding
import com.azatberdimyradov.covid_19app.model.CommonModel


class CountriesAdapter(val context: Context) :
    ListAdapter<CommonModel.Country, CountriesAdapter.CountriesViewHolder>(CountriesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountriesViewHolder {
        val binding = RecyclerViewCountriesItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CountriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CountriesViewHolder, position: Int) {
        val currentItem = getItem(position)
        currentItem?.let {
            holder.bind(currentItem)
        }
    }

    inner class CountriesViewHolder(private val binding: RecyclerViewCountriesItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        //val country = getItem(position)
                            recyclerViewCountryDetailsLayout.layoutAnimation = AnimationUtils.loadLayoutAnimation(context,R.anim.layout_animation)
                        if (recyclerViewCountryDetailsLayout.isVisible) {
                            recyclerViewCountryDetailsLayout.visibility = View.GONE
                            recyclerViewCountriesButton.setImageResource(R.drawable.right_black)
                        } else {
                            recyclerViewCountryDetailsLayout.visibility = View.VISIBLE
                            recyclerViewCountriesButton.setImageResource(R.drawable.down_black)
                        }
                    }
                }
            }
        }

        fun bind(county: CommonModel.Country) {
            binding.apply {
                recyclerViewCountriesItemCountryName.text = county.countryName
                recyclerViewCountryNewConfirmed.text = county.newConfirmed
                recyclerViewCountryTotalConfirmed.text = county.totalConfirmed
                recyclerViewCountryNewDeaths.text = county.newDeaths
                recyclerViewCountryTotalDeaths.text = county.totalDeaths
                recyclerViewCountryNewRecovered.text = county.newRecovered
                recyclerViewCountryTotalRecovered.text = county.totalRecovered
            }
        }
    }

    class CountriesDiffCallback : DiffUtil.ItemCallback<CommonModel.Country>() {
        override fun areItemsTheSame(
            oldItem: CommonModel.Country,
            newItem: CommonModel.Country
        ) = oldItem.countryName == newItem.countryName

        override fun areContentsTheSame(
            oldItem: CommonModel.Country,
            newItem: CommonModel.Country
        ) = oldItem == newItem
    }
}