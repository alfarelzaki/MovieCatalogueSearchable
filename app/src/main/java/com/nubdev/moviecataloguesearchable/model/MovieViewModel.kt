package com.nubdev.moviecataloguesearchable.model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.volley.*
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.nubdev.moviecataloguesearchable.BuildConfig
import com.nubdev.moviecataloguesearchable.dataclass.Movie

class MovieViewModel : ViewModel() {

    companion object {
        private const val API_KEY = BuildConfig.TMDB_API_KEY
    }

    val listMovies = MutableLiveData<ArrayList<Movie>>()

    fun setList(context: Context, lang: String, query: String) {
        val list = ArrayList<Movie>()

        var url = ""
        if (query.isEmpty()) url = "https://api.themoviedb.org/3/discover/movie?api_key=$API_KEY&language=$lang"
        else url = "https://api.themoviedb.org/3/search/movie?api_key=$API_KEY&language=$lang&query=$query"

        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener { response ->
                val jsonArray = response.getJSONArray("results")
                for (i in 0 until jsonArray.length()) {
                    val movie = jsonArray.getJSONObject(i)
                    val id = movie.getString("release_date")
                    val posterPath =  "https://image.tmdb.org/t/p/w342/" + movie.getString("poster_path")
                    val title = movie.getString("original_title")
                    val overview = movie.getString("overview")

                    val contentModel = Movie(title, overview, posterPath, id)
                    list.add(contentModel)
                }
                listMovies.postValue(list)
            },

            Response.ErrorListener { error ->
                Toast.makeText(context, "error data", Toast.LENGTH_LONG).show()

            })

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(jsonObjectRequest)
    }

    fun getList(): LiveData<ArrayList<Movie>> {
        return listMovies
    }

}