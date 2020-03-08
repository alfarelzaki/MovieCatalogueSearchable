package com.nubdev.moviecataloguesearchable.widget

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.os.Binder
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nubdev.moviecataloguesearchable.R
import com.nubdev.moviecataloguesearchable.database.MappingHelper
import com.nubdev.moviecataloguesearchable.database.MovieHelper
import com.nubdev.moviecataloguesearchable.dataclass.Movie
import java.util.*


internal class StackRemoteViewsFactory(private val mContext: Context) : RemoteViewsService.RemoteViewsFactory {

    private var mWidgetItems = ArrayList<Movie>()
    private lateinit var cursor : Cursor
    private lateinit var movieHelper: MovieHelper

    override fun onCreate() {
        movieHelper = MovieHelper(mContext!!)
        movieHelper = MovieHelper.getInstance(mContext!!)
        movieHelper.open()

        Log.d("widget", "created")
    }

    override fun onDataSetChanged() {
        Log.d("widget", "updated")

        val identityToken = Binder.clearCallingIdentity()

        loadMovie()

        Binder.restoreCallingIdentity(identityToken)
    }

    private fun loadMovie() {
        Log.d("widget", "unloaded")
        cursor = movieHelper.queryAll("movie")
        mWidgetItems = MappingHelper.mapCursorToArrayList(cursor)
        Log.d("widget", "loaded")
        Log.d("widget", mWidgetItems.size.toString())
    }

    override fun onDestroy() {

    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)

        Log.d("widget", mWidgetItems[position].poster + position)

        val bitmap: Bitmap = Glide.with(mContext)
                .asBitmap()
                .load(mWidgetItems[position].poster)
                .apply(RequestOptions().fitCenter())
                .submit(800, 550)
                .get()

        rv.setImageViewBitmap(R.id.imageView, bitmap);

        val extras = bundleOf(
                StackWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(i: Int): Long = 0

    override fun hasStableIds(): Boolean = false

}