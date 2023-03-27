package com.example.androidsecondsem.presentation.fragments

import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidsecondsem.R
import com.example.androidsecondsem.data.response.WeatherResponse
import com.example.androidsecondsem.presentation.fragments.viewModel.SearchViewModel
import com.example.androidsecondsem.databinding.FragmentSearchBinding
import com.example.androidsecondsem.presentation.recycler.CityAdapter
import kotlinx.coroutines.launch
import timber.log.Timber

class SearchFragment : Fragment(R.layout.fragment_search) {

    private var binding: FragmentSearchBinding? = null
    private var adapter: CityAdapter? = null
    private var citiesListValue: List<WeatherResponse?>? = null

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModel.Factory
    }

    @RequiresApi(Build.VERSION_CODES.N)
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                lifecycleScope.launch{
                    viewModel.locationPerm(true)
                }
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                lifecycleScope.launch{
                    viewModel.locationPerm(true)
                }
            }
            else -> {
                lifecycleScope.launch{
                    viewModel.locationPerm(false)
                }
            }
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentSearchBinding.bind(view)
        adapter = CityAdapter{viewModel.onWeatherClick(it)}
        binding?.rvTaro?.adapter = adapter
        binding?.rvTaro?.layoutManager = LinearLayoutManager(requireContext())

        observeViewModel()

        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
            return
        } else {
            lifecycleScope.launch {
                viewModel.locationPerm(true)
            }
        }

        binding?.run {
            swCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (query.isNotEmpty()) {
                        viewModel.loadWeather(query)
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })
        }
    }
    private fun navigateToCityFragment(cityId: Int?) {
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.container_of_fragments,
                CityFragment.newInstance(cityId = cityId),
            )
            .addToBackStack(null)
            .commit()
    }
    
    private fun observeViewModel() {
        with(viewModel) {
            citiesList.observe(viewLifecycleOwner) {
                if (it == null) return@observe
                citiesListValue = it
                Timber.e("Залупа ебанная" + citiesListValue.toString())
                setListAdapterConfig()
            }
            location.observe(viewLifecycleOwner) {
                lifecycleScope.launch {
                    viewModel.getCities(it?.latitude, it?.longitude)
                    setListAdapterConfig()
                }
            }
            navigation.observe(viewLifecycleOwner) {
                if (it != null) {
                    navigateToCityFragment(it)
                }
            }
        }
    }

    private fun setListAdapterConfig() {
        Timber.e("жопа")
        binding?.rvTaro?.adapter = adapter
        adapter?.submitList(citiesListValue)

    }

    companion object {
        const val SEARCH_FRAGMENT_TAG = "SEARCH_FRAGMENT_TAG"
        fun newInstance(bundle: Bundle) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putAll(bundle)
                }
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
