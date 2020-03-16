package com.nubdev.consumerfavoritemovie.database

import android.database.Cursor
import android.util.Log
import com.nubdev.consumerfavoritemovie.dataclass.Movie


object MappingHelper {

    fun mapCursorToArrayList(moviesCursor: Cursor?): ArrayList<Movie> {
        val moviesList = ArrayList<Movie>()
        moviesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.MoviesColumns._ID))
                val title = getString(getColumnIndexOrThrow(DatabaseContract.MoviesColumns.TITLE))
                val overview = getString(getColumnIndexOrThrow(DatabaseContract.MoviesColumns.OVERVIEW))
                val date = getString(getColumnIndexOrThrow(DatabaseContract.MoviesColumns.DATE))
                val poster = getString(getColumnIndexOrThrow(DatabaseContract.MoviesColumns.POSTER))
                moviesList.add(Movie(title, overview, poster, date))
                Log.d("widget", title)
            }
        }
        return moviesList
    }
}