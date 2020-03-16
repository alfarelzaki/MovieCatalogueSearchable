package com.nubdev.moviecataloguesearchable.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.nubdev.moviecataloguesearchable.database.DatabaseContract.AUTHORITY
import com.nubdev.moviecataloguesearchable.database.DatabaseContract.MoviesColumns.Companion.TABLE_NAME_MOVIE
import com.nubdev.moviecataloguesearchable.database.DatabaseContract.MoviesColumns.Companion.TABLE_NAME_TVSHOW
import com.nubdev.moviecataloguesearchable.database.MovieHelper

class FavoriteProvider : ContentProvider() {

    companion object {
        private const val MOVIE = 1
        private const val TV = 3
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var movieHelper: MovieHelper
        init {
            // content://com.nubdev.moviecataloguesearchable/movie
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME_MOVIE, MOVIE)
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME_TVSHOW, TV)
        }
    }


    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        TODO("Implement this to handle requests to delete one or more rows")
    }

    override fun getType(uri: Uri): String? {
        TODO(
            "Implement this to handle requests for the MIME type of the data" +
                    "at the given URI"
        )
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        TODO("Implement this to handle requests to insert a new row.")
    }

    override fun onCreate(): Boolean {
        movieHelper = MovieHelper.getInstance(context as Context)
        movieHelper.open()
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        when (sUriMatcher.match(uri)) {
            MOVIE -> return movieHelper.queryAll("movie")
            TV -> return movieHelper.queryAll("tvShow")
            else -> return null
        }
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        TODO("Implement this to handle requests to update one or more rows.")
    }
}
