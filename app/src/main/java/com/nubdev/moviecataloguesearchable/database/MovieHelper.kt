package com.nubdev.moviecataloguesearchable.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.nubdev.moviecataloguesearchable.database.DatabaseContract.MoviesColumns.Companion.TITLE
import com.nubdev.moviecataloguesearchable.database.DatabaseContract.MoviesColumns.Companion._ID
import java.sql.SQLException

class MovieHelper(context: Context) {

    private var dataBaseHelper: DatabaseHelper = DatabaseHelper(context)
    private lateinit var database: SQLiteDatabase

    companion object {
        private var INSTANCE: MovieHelper? = null

        fun getInstance(context: Context): MovieHelper =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: MovieHelper(context)
            }
    }

    @Throws(SQLException::class)
    fun open() {
        database = dataBaseHelper.writableDatabase
    }

    fun close() {
        dataBaseHelper.close()

        if (database.isOpen)
            database.close()
    }

    fun queryAll(tableName: String): Cursor {
        return database.query(
            tableName,
            null,
            null,
            null,
            null,
            null,
            "$_ID ASC",
            null)
    }

    fun queryByTitle(title: String, tableName: String): Cursor {
            return database.query(tableName, null, "$TITLE = ?", arrayOf(title), null, null, null, null)
    }

    fun insert(values: ContentValues?, tableName: String): Long {
        return database.insert(tableName, null, values)
    }

    fun deleteByTitle(title: String, tableName: String): Int {
        return database.delete(tableName, "$TITLE = '$title'", null)
    }
}