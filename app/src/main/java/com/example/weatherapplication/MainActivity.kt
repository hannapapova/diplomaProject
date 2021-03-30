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
    toolbar?.inflateMenu(R.menu.toolbar_home)
    toolbar?.navigationIcon = null
    when (key) {
        "HOME" -> {
            toolbar?.setNavigationIcon(R.drawable.star_border_24)
            toolbar?.menu?.findItem(R.id.toolbar_action)?.setIcon(R.drawable.settings_24)
            toolbar?.menu?.findItem(R.id.toolbar_action)?.isVisible = true
        }
        "FAVORITES" -> {
            toolbar?.menu?.findItem(R.id.toolbar_action)?.setIcon(R.drawable.search_24)
            toolbar?.menu?.findItem(R.id.toolbar_action)?.isVisible = true
        }
        "SETTINGS" -> {
            toolbar?.menu?.findItem(R.id.toolbar_action)?.setIcon(R.drawable.save_24)
            toolbar?.menu?.findItem(R.id.toolbar_action)?.isVisible = true
        }
    }
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
            toolbar?.setOnMenuItemClickListener {
                Navigation.findNavController(view)
                    .navigate(R.id.favoritesFragment_to_searchFragment)
                true
            }
        }
        "SETTINGS" -> {
            toolbar?.setOnMenuItemClickListener {
                //TODO сохранение настроек
                true
            }
        }
    }
}