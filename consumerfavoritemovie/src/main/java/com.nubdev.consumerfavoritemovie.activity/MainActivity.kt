package com.nubdev.consumerfavoritemovie.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.nubdev.consumerfavoritemovie.R
import com.nubdev.consumerfavoritemovie.adapter.SectionsPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sectionsPagerAdapter =
            SectionsPagerAdapter(this, supportFragmentManager)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(R.string.favorites)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true;
        }
        return super.onOptionsItemSelected(item)
    }

}
