package com.example.weatherapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

internal fun setupActionsBar(key: String, view: View, toolbar: Toolbar?) {
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