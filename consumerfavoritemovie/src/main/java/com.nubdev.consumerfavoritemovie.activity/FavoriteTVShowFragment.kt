package com.nubdev.consumerfavoritemovie.activity


import android.content.ContentResolver
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.nubdev.consumerfavoritemovie.R
import com.nubdev.consumerfavoritemovie.adapter.FavoriteTVShowAdapter
import com.nubdev.consumerfavoritemovie.database.DatabaseContract
import com.nubdev.consumerfavoritemovie.database.MappingHelper
import kotlinx.android.synthetic.main.fragment_favorite_movie.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class FavoriteTVShowFragment : Fragment() {

    private lateinit var adapter: FavoriteTVShowAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = FavoriteTVShowAdapter()
        adapter.notifyDataSetChanged()
        loadMovieAsync()
    }

    private fun loadMovieAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressbar_favorite_movies.visibility = View.VISIBLE
            val deferredMovies = async(Dispatchers.IO) {
                val cursor = activity?.contentResolver?.query(
                    DatabaseContract.MoviesColumns.CONTENT_URI_TV, null,
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
