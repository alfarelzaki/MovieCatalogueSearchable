package com.nubdev.moviecataloguesearchable.activity


import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager

import com.nubdev.moviecataloguesearchable.R
import com.nubdev.moviecataloguesearchable.adapter.FavoriteMovieAdapter
import com.nubdev.moviecataloguesearchable.database.DatabaseContract.MoviesColumns.Companion.CONTENT_URI_MOVIE
import com.nubdev.moviecataloguesearchable.database.MappingHelper
import com.nubdev.moviecataloguesearchable.database.MovieHelper
import kotlinx.android.synthetic.main.fragment_favorite_movie.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class FavoriteMovieFragment : Fragment() {

    private lateinit var adapter: FavoriteMovieAdapter
    private lateinit var movieHelper: MovieHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        movieHelper = MovieHelper(context!!)
        movieHelper = MovieHelper.getInstance(context!!)
        movieHelper.open()
        adapter = FavoriteMovieAdapter()
        adapter.notifyDataSetChanged()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadMovieAsync()
            }
        }

        activity?.contentResolver?.registerContentObserver(CONTENT_URI_MOVIE, true, myObserver)

    }

    private fun loadMovieAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressbar_favorite_movies.visibility = View.VISIBLE
            val deferredMovies = async(Dispatchers.IO) {
                val cursor = activity?.contentResolver?.query(CONTENT_URI_MOVIE, null,
                    null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressbar_favorite_movies.visibility = View.INVISIBLE
            val movies = deferredMovies.await()
            if (movies.size > 0) {
                adapter.listMovies = movies
            } else {
                adapter.listMovies = ArrayList()
                adapter.listMovies.clear()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favorite_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_favorite_movies.layoutManager = LinearLayoutManager(activity)
        rv_favorite_movies.setHasFixedSize(true)
        rv_favorite_movies.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        loadMovieAsync()
    }
}
