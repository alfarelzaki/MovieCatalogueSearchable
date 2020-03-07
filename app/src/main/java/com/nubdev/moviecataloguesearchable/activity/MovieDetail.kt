package com.nubdev.moviecataloguesearchable.activity

import android.content.ContentValues
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.nubdev.moviecataloguesearchable.R
import com.nubdev.moviecataloguesearchable.database.DatabaseContract
import com.nubdev.moviecataloguesearchable.database.MovieHelper
import com.nubdev.moviecataloguesearchable.dataclass.Movie
import kotlinx.android.synthetic.main.activity_movie_detail.*

class MovieDetail : AppCompatActivity(), View.OnClickListener{

    companion object {
        var moviePosterURL = ""
    }

    private lateinit var movieHelper: MovieHelper
    private var type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_detail)

        movieHelper = MovieHelper(this)
        movieHelper = MovieHelper.getInstance(applicationContext)
        movieHelper.open()

        type = intent.getStringExtra("type")
        when (type) {
            "movie" -> displayMovie()
            "tvShow" -> displayTVShow()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        showLoading(true)
        button_favorite.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.button_favorite -> {
                val favorites = ContentValues()
                favorites.put(DatabaseContract.MoviesColumns.TITLE, movie_title_detail.text.toString())
                favorites.put(DatabaseContract.MoviesColumns.OVERVIEW, movie_overview_detail.text.toString())
                favorites.put(DatabaseContract.MoviesColumns.DATE, movie_year_detail.text.toString())
                favorites.put(DatabaseContract.MoviesColumns.POSTER, moviePosterURL)
                movieHelper.insert(favorites, type)
                Toast.makeText(this, getString(R.string.added_to_favorite),Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    private fun displayTVShow() {
        val parcelableTVShow = intent.getParcelableExtra<Movie>("parcelable tvShow")
        Glide.with(this)
            .load(parcelableTVShow.poster)
            .into(movie_poster_detail)
        movie_title_detail.text = parcelableTVShow.title
        movie_overview_detail.text = parcelableTVShow.overview
        movie_year_detail.text = parcelableTVShow.year
        supportActionBar?.setTitle(getString(R.string.tvshow_detail))
        moviePosterURL = parcelableTVShow.poster!!
    }

    private fun displayMovie() {
        val parcelableMovie = intent.getParcelableExtra<Movie>("parcelable movie")
        Glide.with(this)
            .load(parcelableMovie.poster)
            .into(movie_poster_detail)
        movie_title_detail.text = parcelableMovie.title
        movie_overview_detail.text = parcelableMovie.overview
        movie_year_detail.text = parcelableMovie.year
        supportActionBar?.setTitle(getString(R.string.movie_detail))
        moviePosterURL = parcelableMovie.poster!!
    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        showLoading(false)
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressbar_detail.visibility = View.VISIBLE
        } else {
            progressbar_detail.visibility = View.GONE
        }
    }
}
