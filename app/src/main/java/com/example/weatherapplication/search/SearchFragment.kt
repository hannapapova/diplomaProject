package com.example.weatherapplication.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.weatherapplication.*
import com.example.weatherapplication.adapter.CitiesAdapter
import com.example.weatherapplication.koin.WeatherViewModel
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import kotlinx.android.synthetic.main.fragment_search.*
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class SearchFragment : Fragment() {
    private val key = "SEARCH"
    private val viewModel by inject<WeatherViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val toolbar = activity?.findViewById<Toolbar>(R.id.toolbar)
        val title = activity?.findViewById<TextView>(R.id.fragment_name)

        setupTitle(title, key)
        setupBackgroundColor(view)
        setupBar(key, toolbar)
//        setupBarActions(key, view, toolbar)
        toolbar?.setNavigationOnClickListener {
            fragmentHideKeyboard(requireContext(), view)
            Navigation.findNavController(view).popBackStack()
        }

        viewModel.favouriteCities.value?.let { viewModel.deleteNotFavouritesFromDB(it) }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.suitableCities.value?.clear()

        viewModel.suitableCities.observe(viewLifecycleOwner, {
            recycler_cities.adapter = CitiesAdapter(it, viewModel)
        })

        Observable.create(ObservableOnSubscribe<String> { subscriber ->
            sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    subscriber.onNext(query)
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    subscriber.onNext(newText)
                    return false
                }
            })
        }).map { text -> text.trim() }
            .debounce(100, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .filter { text -> text.isNotBlank() }
            .subscribe { text ->
                viewModel.getCitiesResult(text)
            }
    }

    override fun onPause() {
        super.onPause()
        viewModel.suitableCities.value?.forEach { suitable ->
            if (suitable.inFavourites)
                viewModel.putSelectedIntoFavouritesDB(suitable)
        }
    }
}