package com.nubdev.moviecataloguesearchable.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.nubdev.moviecataloguesearchable.R
import com.nubdev.moviecataloguesearchable.database.MovieHelper
import kotlinx.android.synthetic.main.activity_bottom_navigation.*


class BottomNavigation : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    companion object {
        var ACTIVE_FRAGMENT = ""
        var CHECK_ACTIVITY = ""
    }

    private lateinit var movieHelper: MovieHelper
    private var arguments = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)

        nav_view.setOnNavigationItemSelectedListener(this)

        if (CHECK_ACTIVITY.isEmpty()) {
            CHECK_ACTIVITY = "intialized"
            nav_view.selectedItemId = R.id.navigation_movies
        }

        movieHelper = MovieHelper(this)
        movieHelper = MovieHelper.getInstance(applicationContext)
        movieHelper.open()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setQueryHint(getString(R.string.query_hint))

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                Log.d("frag", ACTIVE_FRAGMENT)

                // tiap ngesearch ngebikin fragment baru buat nimpa yang lama
                arguments.putString("query", query)

                // inisialisasi ulang fragment dan nimpa
                when (ACTIVE_FRAGMENT) {
                    "movie" -> loadMovieFragment()
                    "tvshow" -> loadTVShowFragment()
                }
                return false
            }

        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_open_favorites) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.action_search) {
            val searchView = item?.actionView as SearchView
            searchView.setFocusable(true)
            searchView.setIconified(false)
        } else if (item.itemId == R.id.action_change_language_settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == android.R.id.home) {
            if (ACTIVE_FRAGMENT == "movie") loadMovieFragment()
            else loadTVShowFragment()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        when (p0.itemId) {
            R.id.navigation_movies -> {
                arguments.putString("query", "")
                loadMovieFragment()
                return true
            }
            R.id.navigation_tvshows -> {
                arguments.putString("query", "")
                loadTVShowFragment()
                return true
            }
        }

        return false
    }

    private fun loadMovieFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        val bottmFragment = MoviesFragment()
        bottmFragment.arguments = arguments
        supportActionBar?.setTitle(getString(R.string.movie_list))
        transaction.replace(R.id.nav_host_fragment, bottmFragment, "TAG_MOVIE").commit()
    }

    private fun loadTVShowFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        val bottmFragment = TVShowsFragment()
        bottmFragment.arguments = arguments
        supportActionBar?.setTitle(getString(R.string.tvshow_list))
        transaction.replace(R.id.nav_host_fragment, bottmFragment, "TAG_TV").commit()
    }
}
