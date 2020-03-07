package com.nubdev.moviecataloguesearchable.database

import android.provider.BaseColumns

internal class DatabaseContract {
    internal class MoviesColumns : BaseColumns {
        companion object {
            const val TABLE_NAME_MOVIE = "movie"
            const val TABLE_NAME_TVSHOW = "tvShow"
            const val _ID = "_id"
            const val TITLE = "title"
            const val OVERVIEW = "overview"
            const val DATE = "date"
            const val POSTER = "poster"
        }
    }
}

