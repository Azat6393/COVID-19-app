package com.azatberdimyradov.covid_19app.view

import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.azatberdimyradov.covid_19app.R
import com.azatberdimyradov.covid_19app.adapter.CountriesAdapter
import com.azatberdimyradov.covid_19app.databinding.FragmentCountriesBinding
import com.azatberdimyradov.covid_19app.model.CommonModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CountriesFragment : Fragment(R.layout.fragment_countries) {

    private lateinit var binding: FragmentCountriesBinding
    private lateinit var countriesAdapter: CountriesAdapter
    private val args: CountriesFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentCountriesBinding.bind(view)

        countriesAdapter = CountriesAdapter(requireContext())
        binding.apply {
            fragmentCountriesRecyclerView.apply {
                adapter = countriesAdapter
                layoutManager = LinearLayoutManager(requireContext())
                setHasFixedSize(true)
            }
            fragmentCountriesSearch.addTextChangedListener {
                val result = arrayListOf<CommonModel.Country>()

                for (country in args.commonModel.countries) {
                    if (country.countryName.toLowerCase().contains(it.toString())) {
                        result.add(country)
                    }
                }
                countriesAdapter.submitList(result)
            }
        }
        countriesAdapter.submitList(args.commonModel.countries)
    }
}