package com.nubdev.moviecataloguesearchable.activity

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.nubdev.moviecataloguesearchable.R
import kotlinx.android.synthetic.main.activity_settings.*
import com.nubdev.moviecataloguesearchable.receiver.ReminderReceiver
import com.nubdev.moviecataloguesearchable.receiver.ReminderReceiver.Companion.EXTRA_DATE
import com.nubdev.moviecataloguesearchable.receiver.ReminderReceiver.Companion.EXTRA_TYPE
import java.text.SimpleDateFormat
import java.util.*

class SettingsActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        const val PREF_SWITCH_DAILY_ALARM = "daily alarm"
        const val PREF_SWITCH_DAILY_NEW = "daily new"
    }

    private lateinit var alarmManagerDaily : AlarmManager
    private lateinit var alarmManagerNewMovies : AlarmManager
    private lateinit var dailyAlarmIntent : Intent
    private lateinit var newMoviesAlarmIntent : Intent
    private lateinit var pendingIntentDaily : PendingIntent
    private lateinit var pendingIntentNewMovies : PendingIntent
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        alarmManagerDaily = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManagerNewMovies = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        // repeating alarm
        dailyAlarmIntent = Intent(this, ReminderReceiver::class.java)
        dailyAlarmIntent.putExtra(EXTRA_TYPE, "daily")
        pendingIntentDaily = PendingIntent.getBroadcast(
            this, ReminderReceiver.ID_REPEATING, dailyAlarmIntent, 0
        )

        // new movies alarm
        newMoviesAlarmIntent = Intent(this, ReminderReceiver::class.java)
        newMoviesAlarmIntent.putExtra(EXTRA_TYPE, "new_movies")
        pendingIntentNewMovies = PendingIntent.getBroadcast(
            this, ReminderReceiver.ID_NEW_MOVIES, newMoviesAlarmIntent, 0
        )

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setTitle(getString(R.string.settings_activity))

        layout_settings.setOnClickListener(this)

        onSwitch()
    }

    private fun onSwitch() {
        val sharedPreferences : SharedPreferences = getSharedPreferences("switch settings", MODE_PRIVATE)
        val editor : SharedPreferences.Editor = sharedPreferences.edit()

        alarm_interval_day_settings_switch.isChecked = sharedPreferences.getBoolean(
            PREF_SWITCH_DAILY_ALARM, false)
        alarm_interval_day_settings_switch.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                editor.putBoolean(PREF_SWITCH_DAILY_ALARM, true)
                setRepeatingAlarm()
                Toast.makeText(this, getString(R.string.daily_reminder_on), Toast.LENGTH_SHORT).show()
            } else {
                editor.putBoolean(PREF_SWITCH_DAILY_ALARM, false)
                alarmManagerDaily.cancel(pendingIntentDaily)
                Toast.makeText(this, getString(R.string.daily_reminder_off), Toast.LENGTH_SHORT).show()
            }
            editor.apply()
        }

        alarm_new_movie_settings_switch.isChecked = sharedPreferences.getBoolean(PREF_SWITCH_DAILY_NEW, false)
        alarm_new_movie_settings_switch.setOnCheckedChangeListener { compoundButton, isChecked ->
            if (isChecked) {
                editor.putBoolean(PREF_SWITCH_DAILY_NEW, true)
                setNewMoviesAlarm()
                Toast.makeText(this, getString(R.string.movie_release_reminder_on), Toast.LENGTH_SHORT).show()
            } else {
                editor.putBoolean(PREF_SWITCH_DAILY_NEW, false)
                alarmManagerNewMovies.cancel(pendingIntentNewMovies)
                Toast.makeText(this, getString(R.string.movie_release_reminder_off), Toast.LENGTH_SHORT).show()
            }
            editor.apply()
        }
    }

    fun setRepeatingAlarm() {
        val calendar = Calendar.getInstance()
        if (calendar.get(Calendar.HOUR_OF_DAY)>=7 && calendar.get(Calendar.MINUTE)>0) calendar.add(Calendar.DATE, 1)
        calendar.set(Calendar.HOUR_OF_DAY, 7)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        // alarm manager
        alarmManagerDaily.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntentDaily
        )

        Log.d("type", calendar.toString())
    }

    fun setNewMoviesAlarm() {
        if (calendar.get(Calendar.HOUR_OF_DAY)>=8 && calendar.get(Calendar.MINUTE)>0) calendar.add(Calendar.DATE, 1)

        calendar.set(Calendar.HOUR_OF_DAY, 8)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        // alarm manager
        alarmManagerNewMovies.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntentNewMovies
        )

        Log.d("type", calendar.toString())
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.layout_settings -> {
                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                startActivity(mIntent)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true;
        }
        return super.onOptionsItemSelected(item)
    }
}
