package com.nubdev.moviecataloguesearchable.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.nubdev.moviecataloguesearchable.database.DatabaseContract.MoviesColumns.Companion.TABLE_NAME_MOVIE
import com.nubdev.moviecataloguesearchable.database.DatabaseContract.MoviesColumns.Companion.TABLE_NAME_TVSHOW

internal class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION){
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_TABLE_MOVIE)
        db?.execSQL(SQL_CREATE_TABLE_TVSHOW)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_MOVIE")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_TVSHOW")
        onCreate(db)
    }

    companion object {
        private const val DATABASE_NAME = "dbmovieapp"
        private const val DATABASE_VERSION = 1
        private val SQL_CREATE_TABLE_MOVIE = "CREATE TABLE $TABLE_NAME_MOVIE" +
                " (${DatabaseContract.MoviesColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.MoviesColumns.TITLE} TEXT NOT NULL UNIQUE," +
                " ${DatabaseContract.MoviesColumns.OVERVIEW} TEXT NOT NULL," +
                " ${DatabaseContract.MoviesColumns.DATE} TEXT NOT NULL," +
                " ${DatabaseContract.MoviesColumns.POSTER} TEXT NOT NULL)"

        private val SQL_CREATE_TABLE_TVSHOW = "CREATE TABLE $TABLE_NAME_TVSHOW" +
                " (${DatabaseContract.MoviesColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.MoviesColumns.TITLE} TEXT NOT NULL UNIQUE," +
                " ${DatabaseContract.MoviesColumns.OVERVIEW} TEXT NOT NULL," +
                " ${DatabaseContract.MoviesColumns.DATE} TEXT NOT NULL," +
                " ${DatabaseContract.MoviesColumns.POSTER} TEXT NOT NULL)"
    }
}