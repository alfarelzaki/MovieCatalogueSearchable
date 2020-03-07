package com.nubdev.moviecataloguesearchable.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.nubdev.moviecataloguesearchable.R
import com.nubdev.moviecataloguesearchable.adapter.ListMovieAdapter
import com.nubdev.moviecataloguesearchable.callapi.MainViewModel
import com.nubdev.moviecataloguesearchable.database.MovieHelper
import kotlinx.android.synthetic.main.activity_bottom_navigation.*
import kotlinx.android.synthetic.main.fragment_movies.*
import receiver.ReminderReceiver
import java.util.*


class BottomNavigation : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    companion object {
        var ACTIVE_FRAGMENT = ""
    }

    private lateinit var movieHelper: MovieHelper
    private lateinit var adapter: ListMovieAdapter
    private lateinit var mainViewModel: MainViewModel
    private var arguments = Bundle()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)

        nav_view.setOnNavigationItemSelectedListener(this)
        firstInitial()

        movieHelper = MovieHelper(this)
        movieHelper = MovieHelper.getInstance(applicationContext)
        movieHelper.open()

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java
        )

        // still not fixed :)
        setRepeatingAlarm()
    }

    private fun firstInitial() {
        val transaction = supportFragmentManager.beginTransaction()
        val bottmFragment = MoviesFragment()
        bottmFragment.arguments = arguments
        transaction.add(R.id.nav_host_fragment, bottmFragment, "TAG_FIRST").addToBackStack(null).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as SearchView
        searchView.setQueryHint("Search")

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
        if (item.itemId == R.id.action_change_language_settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
//            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
//            startActivity(mIntent)
        } else if (item.itemId == R.id.action_open_favorites) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else if (item.itemId == R.id.action_search) {
            val searchView = item?.actionView as SearchView
            searchView.setFocusable(true)
            searchView.setIconified(false)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressbar.visibility = View.VISIBLE
        } else {
            progressbar.visibility = View.GONE
        }
    }

    fun setRepeatingAlarm() {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 12)
        calendar.set(Calendar.MINUTE, 50)
        calendar.set(Calendar.SECOND, 0)

        val intent = Intent(this, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            this, ReminderReceiver.ID_REPEATING, intent, 0
        )

        // alarm manager
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        var f: Fragment? = supportFragmentManager.findFragmentByTag("TAG_FIRST")
        if (f != null) supportFragmentManager.beginTransaction().remove(f).commit()

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

    private fun loadTVShowFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        val bottmFragment = TVShowsFragment()
        bottmFragment.arguments = arguments
        transaction.replace(R.id.nav_host_fragment, bottmFragment, "TAG_TV").addToBackStack(null).commit()
    }

    private fun loadMovieFragment() {
        val transaction = supportFragmentManager.beginTransaction()
        val bottmFragment = MoviesFragment()
        bottmFragment.arguments = arguments
        transaction.replace(R.id.nav_host_fragment, bottmFragment, "TAG_MOVIE").addToBackStack(null).commit()
    }
}
