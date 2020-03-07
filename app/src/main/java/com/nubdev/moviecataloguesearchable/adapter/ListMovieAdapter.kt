package com.nubdev.moviecataloguesearchable.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nubdev.moviecataloguesearchable.R
import com.nubdev.moviecataloguesearchable.activity.MovieDetail
import com.nubdev.moviecataloguesearchable.dataclass.Movie
import kotlinx.android.synthetic.main.item_row_movie.view.*

class ListMovieAdapter : RecyclerView.Adapter<ListMovieAdapter.ListViewHolder>() {
    private val mData = ArrayList<Movie>()

    fun setData(items: ArrayList<Movie>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_row_movie, parent, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return mData.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(mData[position])
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(movie.poster)
                    .into(movie_poster)

                movie_title.text = movie.title

                if (movie.overview.toString().isEmpty()) movie.overview = context.getString(R.string.empty_note)
                movie_overview.text = movie.overview

                itemView.setOnClickListener{
                    val intent = Intent(context, MovieDetail::class.java)
                    val movieParcelable = Movie(
                        movie.title,
                        movie.overview,
                        movie.poster,
                        movie.year
                    )
                    intent.putExtra("parcelable movie", movieParcelable)
                    intent.putExtra("type", "movie")
                    context.startActivity(intent)
                }
            }
        }
    }
}