package com.example.androidsecondsem.presentation.fragments

import android.os.Bundle
import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import android.widget.SearchView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidsecondsem.R
import com.example.androidsecondsem.data.weather.response.WeatherResponse
import com.example.androidsecondsem.presentation.fragments.viewModel.SearchViewModel
import com.example.androidsecondsem.databinding.FragmentSearchBinding
import com.example.androidsecondsem.presentation.recycler.CityAdapter
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.Flowables
import io.reactivex.rxjava3.kotlin.subscribeBy
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private var binding: FragmentSearchBinding? = null
    private var adapter: CityAdapter? = null
    private var citiesListValue: List<WeatherResponse?>? = null
    private var searchDisposable: Disposable? = null

    private val viewModel: SearchViewModel by viewModels()

    @RequiresApi(Build.VERSION_CODES.N)
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                Single.fromCallable { viewModel.locationPerm(true) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                Single.fromCallable { viewModel.locationPerm(true) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
            }
            else -> {
                Single.fromCallable { viewModel.locationPerm(false) }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe()
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
            Single.fromCallable { viewModel.locationPerm(true) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        }

        binding?.run {
            searchDisposable = observeQuery()
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .distinctUntilChanged()
                .subscribeBy(onNext = {
                    Timber.e(it)
                    viewModel.loadWeather(it)
                }, onError = {
                    Timber.e(it)
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
                setListAdapterConfig()
            }
            location.observe(viewLifecycleOwner) {
                lifecycleScope.launch {
                    viewModel.getCities(it?.latitude, it?.longitude)
                    Log.e(it?.latitude.toString(), it?.latitude.toString())
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
        binding?.rvTaro?.adapter = adapter
        adapter?.submitList(citiesListValue)
    }

    private fun observeQuery() =
        Flowables.create(mode = BackpressureStrategy.LATEST) { emitter ->
            binding?.swCity?.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText?.length.toString().toInt()  > 1)
                        emitter.onNext(newText ?: "")
                    return false
                }
            })
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

    override fun onResume() {
        super.onResume()
        binding?.swCity?.setQuery("", false)
        binding?.swCity?.clearFocus()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
        searchDisposable?.dispose()
    }
}
