package com.example.weatherapplication

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import com.example.weatherapplication.koin.WeatherViewModel
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val viewModel by inject<WeatherViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(R.layout.activity_main)
    }
}

internal fun setupBar(key: String, toolbar: Toolbar?) {
    toolbar?.menu?.clear()
    when (key) {
        "HOME" -> {
            toolbar?.inflateMenu(R.menu.toolbar_home)
            toolbar?.setNavigationIcon(R.drawable.star_border_24)
        }
        "FAVORITES" -> {
            addBackButton(toolbar)
            toolbar?.menu?.findItem(R.id.action)?.isVisible = true
            toolbar?.menu?.findItem(R.id.action)?.setIcon(R.drawable.search_24)
        }
        "DETAILS" -> {
            addBackButton(toolbar)
        }
        "SEARCH" -> {
            addBackButton(toolbar)
        }
        "SETTINGS" -> {
            addBackButton(toolbar)
            toolbar?.menu?.findItem(R.id.action)?.isVisible = true
            toolbar?.menu?.findItem(R.id.action)?.setIcon(R.drawable.save_24)
        }
    }
}

private fun addBackButton(toolbar: Toolbar?) {
    toolbar?.inflateMenu(R.menu.back_menu_with_search)
    toolbar?.setNavigationIcon(R.drawable.arrow_back_24)
}

internal fun setupBarActions(key: String, view: View, toolbar: Toolbar?) {
    when (key) {
        "HOME" -> {
            toolbar?.setNavigationOnClickListener {
                Navigation.findNavController(view).navigate(R.id.homeFragment_to_favoritesFragment)
            }
            toolbar?.setOnMenuItemClickListener {
                Navigation.findNavController(view)
                    .navigate(R.id.homeFragment_to_settingsFragment)
                true
            }
        }
        "FAVORITES" -> {
            toolbar?.setNavigationOnClickListener {
                Navigation.findNavController(view).popBackStack()
            }
            toolbar?.setOnMenuItemClickListener {
                Navigation.findNavController(view)
                    .navigate(R.id.favoritesFragment_to_searchFragment)
                true
            }
        }
        "DETAILS" -> {
            toolbar?.setNavigationOnClickListener {
                Navigation.findNavController(view).popBackStack()
            }
        }
        "SEARCH" -> {
            toolbar?.setNavigationOnClickListener {
                Navigation.findNavController(view).popBackStack()
            }
        }
        "SETTINGS" -> {
            toolbar?.setNavigationOnClickListener {
                Navigation.findNavController(view).popBackStack()
            }
            toolbar?.setOnMenuItemClickListener {
                //TODO Save settings
                true
            }
        }
    }
}

internal fun setupTitle(title: TextView?, key: String) {
    when (key) {
        "HOME" -> {
            //TODO Set town name
            title?.text = "Home"
        }
        "FAVORITES" -> {
            title?.text = "Favorite"
        }
        "DETAILS" -> {
            title?.text = "Details"
        }
        "SEARCH" -> {
            title?.text = "Search"
        }
        "SETTINGS" -> {
            title?.text = "Settings"
        }
    }
}

internal fun setupStatusBarColor(window: Window?, context: Context) {
    //TODO Set color depending on weather status
    window?.statusBarColor = ContextCompat.getColor(context,R.color.end_color_storm)
}

internal fun setupBackgroundColor(view: View) {
    //TODO Set background depending on weather status
    view.setBackgroundResource(R.drawable.storm_gradient_bg)
}

internal fun setupToolBarBackgroundColor(toolbar: Toolbar?, context: Context) {
    //TODO Set color depending on weather status
    toolbar?.setBackgroundColor(ContextCompat.getColor(context, R.color.end_color_storm))
}