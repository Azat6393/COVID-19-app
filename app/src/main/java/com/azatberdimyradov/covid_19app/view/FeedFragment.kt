package com.azatberdimyradov.covid_19app.view

import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.azatberdimyradov.covid_19app.R
import com.azatberdimyradov.covid_19app.databinding.FragmentFeedBinding
import com.azatberdimyradov.covid_19app.model.CommonModel
import com.azatberdimyradov.covid_19app.viewmodel.FeedViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.coroutines.flow.collect
import org.eazegraph.lib.models.PieModel

@AndroidEntryPoint
class FeedFragment : Fragment(R.layout.fragment_feed) {

    private lateinit var binding: FragmentFeedBinding
    private val viewModel by viewModels<FeedViewModel>()
    private lateinit var commonModel: CommonModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFeedBinding.bind(view)

        binding.apply {
            radioGroupOne.setOnCheckedChangeListener(listener1)
            radioGroupTwo.setOnCheckedChangeListener(listener2)
            radioGroupThree.setOnCheckedChangeListener(listener3)

            trackCountriesButton.setOnClickListener {
                val action =
                    FeedFragmentDirections.actionFeedFragmentToCountriesFragment(commonModel)
                findNavController().navigate(action)
            }
            retryButton.setOnClickListener {
                viewModel.getData()
                retryButton.visibility = View.INVISIBLE
                counterProgressBar.visibility = View.VISIBLE
                perChartProgressBar.visibility = View.VISIBLE
            }
            updateTextColor()
        }
        getDataFromViewModel()
        observe()
        eventChannel()
    }

    private fun eventChannel() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.feedEvent.collect { event ->
                when (event) {
                    is FeedViewModel.FeedEvent.OnError -> {
                        Snackbar.make(requireView(), event.message, Snackbar.LENGTH_LONG).show()
                        binding.apply {
                            counterProgressBar.visibility = View.INVISIBLE
                            perChartProgressBar.visibility = View.INVISIBLE
                            retryButton.visibility = View.VISIBLE
                        }
                    }
                    is FeedViewModel.FeedEvent.OnSuccess -> {
                        onSuccess()
                    }
                }
            }
        }
    }

    private fun getDataFromViewModel() {
        if (viewModel.commonModel.value == null) {
            viewModel.getData()
        } else {
            viewModel.commonModel.value?.let {
                onSuccess()
                commonModel = it
                setCounterText(commonModel.global.newConfirmed)
                customizePieChart()
            }
        }
    }

    private fun observe() {
        viewModel.commonModel.observe(viewLifecycleOwner) {
            commonModel = it
            setCounterText(commonModel.global.newConfirmed)
            customizePieChart()
        }
    }

    private fun onSuccess() {
        binding.apply {
            counterProgressBar.visibility = View.INVISIBLE
            perChartProgressBar.visibility = View.INVISIBLE
            counterTextView.visibility = View.VISIBLE
            piechart.visibility = View.VISIBLE
            trackCountriesButton.isEnabled = true
        }
    }

    private fun customizePieChart() {
        binding.apply {
            piechart.clearChart()
            piechart.addPieSlice(
                PieModel(
                    "Total confirmed",
                    commonModel.global.totalConfirmed.toFloat(),
                    resources.getColor(R.color.totalConfirmed)
                )
            )
            piechart.addPieSlice(
                PieModel(
                    "Total deaths",
                    commonModel.global.totalDeaths.toFloat(),
                    resources.getColor(R.color.totalDeaths)
                )
            )
            piechart.addPieSlice(
                PieModel(
                    "Total recovered",
                    commonModel.global.totalRecovered.toFloat(),
                    resources.getColor(R.color.totalRecovered)
                )
            )
            piechart.startAnimation()
        }
    }

    private val listener1: RadioGroup.OnCheckedChangeListener =
        RadioGroup.OnCheckedChangeListener { group, checkedId ->
            binding.apply {
                radioGroupTwo.setOnCheckedChangeListener(null)
                radioGroupTwo.clearCheck()
                radioGroupTwo.setOnCheckedChangeListener(listener2)
                radioGroupThree.setOnCheckedChangeListener(null)
                radioGroupThree.clearCheck()
                radioGroupThree.setOnCheckedChangeListener(listener3)
                checked(checkedId)
            }
        }
    private val listener2: RadioGroup.OnCheckedChangeListener =
        RadioGroup.OnCheckedChangeListener { group, checkedId ->
            binding.apply {
                radioGroupOne.setOnCheckedChangeListener(null)
                radioGroupOne.clearCheck()
                radioGroupOne.setOnCheckedChangeListener(listener1)
                radioGroupThree.setOnCheckedChangeListener(null)
                radioGroupThree.clearCheck()
                radioGroupThree.setOnCheckedChangeListener(listener3)
                checked(checkedId)
            }
        }

    private val listener3: RadioGroup.OnCheckedChangeListener =
        RadioGroup.OnCheckedChangeListener { group, checkedId ->
            binding.apply {
                radioGroupOne.setOnCheckedChangeListener(null)
                radioGroupOne.clearCheck()
                radioGroupOne.setOnCheckedChangeListener(listener1)
                radioGroupTwo.setOnCheckedChangeListener(null)
                radioGroupTwo.clearCheck()
                radioGroupTwo.setOnCheckedChangeListener(listener2)
                checked(checkedId)
            }
        }

    private fun checked(checkedId: Int) {
        updateTextColor()
        when (checkedId) {
            R.id.radio_button_new_confirmed -> {
                setCounterText(commonModel.global.newConfirmed)
            }
            R.id.radio_button_total_confirmed -> {
                setCounterText(commonModel.global.totalConfirmed)
            }
            R.id.radio_button_new_deaths -> {
                setCounterText(commonModel.global.newDeaths)
            }
            R.id.radio_button_total_deaths -> {
                setCounterText(commonModel.global.totalDeaths)
            }
            R.id.radio_button_new_recovered -> {
                setCounterText(commonModel.global.newRecovered)
            }
            R.id.radio_button_total_recovered -> {
                setCounterText(commonModel.global.totalRecovered)
            }
        }
    }

    private fun updateTextColor() {
        if (binding.radioButtonNewConfirmed.isChecked) {
            binding.radioButtonNewConfirmed.setTextColor(resources.getColor(R.color.white))
        } else binding.radioButtonNewConfirmed.setTextColor(resources.getColor(R.color.newConfirmed))

        if (binding.radioButtonTotalConfirmed.isChecked) {
            binding.radioButtonTotalConfirmed.setTextColor(resources.getColor(R.color.white))
        } else binding.radioButtonTotalConfirmed.setTextColor(resources.getColor(R.color.totalConfirmed))

        if (binding.radioButtonNewDeaths.isChecked) {
            binding.radioButtonNewDeaths.setTextColor(resources.getColor(R.color.white))
        } else binding.radioButtonNewDeaths.setTextColor(resources.getColor(R.color.newDeaths))

        if (binding.radioButtonTotalDeaths.isChecked) {
            binding.radioButtonTotalDeaths.setTextColor(resources.getColor(R.color.white))
        } else binding.radioButtonTotalDeaths.setTextColor(resources.getColor(R.color.totalDeaths))

        if (binding.radioButtonNewRecovered.isChecked) {
            binding.radioButtonNewRecovered.setTextColor(resources.getColor(R.color.white))
        } else binding.radioButtonNewRecovered.setTextColor(resources.getColor(R.color.newRecovered))

        if (binding.radioButtonTotalRecovered.isChecked) {
            binding.radioButtonTotalRecovered.setTextColor(resources.getColor(R.color.white))
        } else binding.radioButtonTotalRecovered.setTextColor(resources.getColor(R.color.totalRecovered))
    }

    private fun setCounterText(text: String) {
        binding.counterTextView.text = text
    }

    override fun onStop() {
        super.onStop()
        binding.apply {
            radioGroupOne.clearCheck()
            radioGroupTwo.clearCheck()
            radioGroupThree.clearCheck()
            radioButtonNewConfirmed.isChecked = true
        }
    }
}