package com.example.aplikasidicodingeventnavigationdanapi

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.aplikasidicodingeventnavigationdanapi.databinding.ActivityMainBinding
import com.example.aplikasidicodingeventnavigationdanapi.ui.finished.FinishedFragment.Companion.EXTRA_QUERY2
import com.example.aplikasidicodingeventnavigationdanapi.ui.upcoming.UpcomingFragment
import com.example.aplikasidicodingeventnavigationdanapi.ui.upcoming.UpcomingFragment.Companion.EXTRA_QUERY
import com.example.aplikasidicodingeventnavigationdanapi.ui.upcoming.UpcomingViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity(), SearchView.OnQueryTextListener {
    private lateinit var binding: ActivityMainBinding

    private lateinit var upcomingViewModel: UpcomingViewModel
    private lateinit var adapter: EventAdapter
    private lateinit var upcomingFragment: UpcomingFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // search
        upcomingViewModel = ViewModelProvider(this)[UpcomingViewModel::class.java]
        upcomingFragment = UpcomingFragment()
        adapter = EventAdapter()
        upcomingViewModel.listEventsItemUpcoming.observe(this) { events ->
            adapter.submitList(events ?: emptyList())
        }

        val navView: BottomNavigationView = binding.bottomNavigation

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home_menu, R.id.upcoming_menu, R.id.finished_menu
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)

        val search = menu.findItem(R.id.menu_search)
        val searchView = search?.actionView as? SearchView

        // search button gone in home fragment
        val navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            search?.isVisible = destination.id != R.id.home_menu
        }
        searchView?.let {
            it.queryHint = "Search Event"
            it.isSubmitButtonEnabled = true
            it.setOnQueryTextListener(this)
        }
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        query?.let {
            Log.d("SearchTestMain", "Search query submitted: $it")
            try {
                val navController = findNavController(R.id.nav_host_fragment)
                val bundle = Bundle().apply {
                    putString(EXTRA_QUERY, it)
                }
                val bundle2 = Bundle().apply {
                    putString(EXTRA_QUERY2, it)
                }
                when (navController.currentDestination?.id) {
                    R.id.home_menu -> navController.navigate(R.id.home_menu, bundle)
                    R.id.upcoming_menu -> navController.navigate(R.id.upcoming_menu, bundle)
                    R.id.finished_menu -> navController.navigate(R.id.finished_menu, bundle2)
                    else -> Log.e("SearchTestMain", "Unknown destination")
                }

                // Hide the keyboard
                val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
                currentFocus?.clearFocus()

                Log.d("SearchTestMain", "Search event triggered successfully")
            } catch (e: Exception) {
                Log.e("SearchTestMain", "Error triggering search event", e)
            }
        } ?: run {
            Log.d("SearchTestMain", "Search query is null")
        }
        Toast.makeText(this, "Searching $query", Toast.LENGTH_SHORT).show()
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query.isNullOrEmpty()) {
            Log.d("SearchTestMain", "Search query is empty or null")
            // Handler when query is empty return to original event
            val navController = findNavController(R.id.nav_host_fragment)
            val bundle = Bundle().apply {
                putString(EXTRA_QUERY, "%default")
            }
            val bundle2 = Bundle().apply {
                putString(EXTRA_QUERY2, "%default")
            }
            when (navController.currentDestination?.id) {
                R.id.home_menu -> navController.navigate(R.id.home_menu, bundle)
                R.id.upcoming_menu -> navController.navigate(R.id.upcoming_menu, bundle)
                R.id.finished_menu -> navController.navigate(R.id.finished_menu, bundle2)
                else -> Log.e("SearchTestMain", "Unknown destination")
            }
            Toast.makeText(this, "Return to default Value", Toast.LENGTH_SHORT).show()
        }
        return true
    }
}