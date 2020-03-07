package com.nubdev.moviecataloguesearchable.activity


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nubdev.moviecataloguesearchable.R
import com.nubdev.moviecataloguesearchable.activity.BottomNavigation.Companion.ACTIVE_FRAGMENT
import com.nubdev.moviecataloguesearchable.adapter.ListTVShowAdapter
import com.nubdev.moviecataloguesearchable.callapi.MainViewModelTVShow
import com.nubdev.moviecataloguesearchable.dataclass.Movie
import kotlinx.android.synthetic.main.fragment_movies.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class TVShowsFragment : Fragment() {

    private lateinit var adapter: ListTVShowAdapter
    private lateinit var mainViewModel: MainViewModelTVShow
    protected lateinit var rootView: View
    private lateinit var rvMain: RecyclerView

    // query searchable
    var query : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ACTIVE_FRAGMENT = "tvshow"

        retainInstance = true

        var arguments = getArguments()
        query = arguments?.getString("query")
        if (query == null) query = ""
        Log.d("query", query)

        mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModelTVShow::class.java)
    }

    private fun setAdapter() {
        adapter = ListTVShowAdapter()
        adapter.notifyDataSetChanged()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_movies, container, false)
        setAdapter()
        showRecyclerList()
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setMovieList()
        activity?.actionBar?.setTitle(R.string.tvshow_list)
    }

    private fun setMovieList() {
        var lang = Locale.getDefault().toLanguageTag()

        mainViewModel.setMovies(lang, query.toString())
        showLoading(true)

        mainViewModel.getMovies().observe(this, Observer { movieItems ->
            if (movieItems != null) {
                adapter.setData(movieItems)
                showLoading(false)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        setMovieList()
        adapter.notifyDataSetChanged()
    }

    private fun showRecyclerList() {
        rvMain = rootView.findViewById<RecyclerView>(R.id.rv_main)
        rvMain.layoutManager = LinearLayoutManager(context)
        rvMain.adapter = adapter
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressbar.visibility = View.VISIBLE
        } else {
            progressbar.visibility = View.GONE
        }
    }
}
