package com.nubdev.moviecataloguesearchable.callapi

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.nubdev.moviecataloguesearchable.BuildConfig
import com.nubdev.moviecataloguesearchable.dataclass.Movie
import cz.msebera.android.httpclient.Header
import org.json.JSONObject

class MainViewModel : ViewModel() {
    companion object {
        private const val API_KEY = BuildConfig.TMDB_API_KEY
    }

    val listMovies = MutableLiveData<ArrayList<Movie>>()

    internal fun setMovies(lang: String, query: String) {
        val client = AsyncHttpClient()
        val listItems = ArrayList<Movie>()

        Log.d("quora", query)

        var url = ""
        if (query.isEmpty()) url = "https://api.themoviedb.org/3/discover/movie?api_key=$API_KEY&language=$lang"
        else url = "https://api.themoviedb.org/3/search/movie?api_key=$API_KEY&language=$lang&query=$query"

        Log.d("movies api", url)
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray
            ) {
                try {
                    val result = String(responseBody)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("results")
                    for (i in 0 until list.length()) {
                        val movie = list.getJSONObject(i)
                        val movieItems = Movie()
                        var posterUrl = movie.getString("poster_path")
                        movieItems.poster = "https://image.tmdb.org/t/p/w342/$posterUrl"
                        movieItems.title = movie.getString("title")
                        movieItems.overview = movie.getString("overview")
                        movieItems.year = movie.getString("release_date")
                        listItems.add(movieItems)
                    }
                    listMovies.postValue(listItems)
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<Header>,
                responseBody: ByteArray,
                error: Throwable
            ) {
                Log.d("onFailure", error.message.toString())
            }
        })
    }

    internal fun getMovies(): LiveData<ArrayList<Movie>> {
        return listMovies
    }
}