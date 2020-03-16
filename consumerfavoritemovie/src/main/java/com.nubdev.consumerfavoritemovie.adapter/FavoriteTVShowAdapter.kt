package com.nubdev.consumerfavoritemovie.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nubdev.consumerfavoritemovie.R
import com.nubdev.consumerfavoritemovie.dataclass.Movie
import kotlinx.android.synthetic.main.movie_favorite_item.view.*

class FavoriteTVShowAdapter : RecyclerView.Adapter<FavoriteTVShowAdapter.FavoriteTVShowViewHolder>() {

    var listMovies = ArrayList<Movie>()
        set(listMovies) {
            if (listMovies.size > 0) {
                this.listMovies.clear()
            }
            this.listMovies.addAll(listMovies)
            notifyDataSetChanged()
        }

    fun addItem(movie: Movie) {
        this.listMovies.add(movie)
        notifyItemInserted(this.listMovies.size - 1)
    }

    fun removeItem(position: Int) {
        this.listMovies.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listMovies.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteTVShowViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.movie_favorite_item, parent, false)
        return FavoriteTVShowViewHolder(view)
    }
    override fun onBindViewHolder(holder: FavoriteTVShowViewHolder, position: Int) {
        holder.bind(listMovies[position])
    }
    override fun getItemCount(): Int {
        return this.listMovies.size
    }
    inner class FavoriteTVShowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(movie: Movie) {
            with(itemView){
                favorite_movie_title.text = movie.title
                if (movie.overview.toString().isEmpty()) movie.overview = context.getString(R.string.empty_note)
                favorite_movie_overview.text = movie.overview
                Glide.with(itemView.context)
                    .load(movie.poster)
                    .into(favorite_movie_poster)
            }
        }
    }
}