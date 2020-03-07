package com.nubdev.moviecataloguesearchable.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.nubdev.moviecataloguesearchable.R
import com.nubdev.moviecataloguesearchable.database.MovieHelper
import com.nubdev.moviecataloguesearchable.dataclass.Movie
import kotlinx.android.synthetic.main.activity_favorite_detail.*

class FavoriteDetailActivity : AppCompatActivity(), View.OnClickListener {

    private var movie: Movie? = null
    private var position: Int = 0
    private var title: String = ""
    private lateinit var movieHelper: MovieHelper
    private var type: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_detail)

        movieHelper = MovieHelper(this)
        movieHelper = MovieHelper.getInstance(applicationContext)
        movieHelper.open()

        type = intent.getStringExtra("type")
        when (type) {
            "movie" -> displayFavoriteMovie()
            "tvShow" -> displayFavoriteTVShow()
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        button_remove_favorite.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id){
            R.id.button_remove_favorite -> {
                movieHelper.deleteByTitle(title, type)
                Toast.makeText(this, getString(R.string.removed_from_favorite), Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun displayFavoriteMovie() {
        val parcelableMovie = intent.getParcelableExtra<Movie>("parcelable movie")
        Glide.with(this)
            .load(parcelableMovie.poster)
            .into(favorite_movie_poster_detail)
        favorite_movie_title_detail.text = parcelableMovie.title
        favorite_movie_overview_detail.text = parcelableMovie.overview
        favorite_movie_year_detail.text = parcelableMovie.year
        supportActionBar?.setTitle(getString(R.string.movie_detail_favorite))
        title = parcelableMovie.title.toString()
    }

    fun displayFavoriteTVShow() {
        val parcelableTVShow = intent.getParcelableExtra<Movie>("parcelable tvShow")
        Glide.with(this)
            .load(parcelableTVShow.poster)
            .into(favorite_movie_poster_detail)
        favorite_movie_title_detail.text = parcelableTVShow.title
        favorite_movie_overview_detail.text = parcelableTVShow.overview
        favorite_movie_year_detail.text = parcelableTVShow.year
        supportActionBar?.setTitle(getString(R.string.tvshow_detail_favorite))
        title = parcelableTVShow.title.toString()
    }
}
