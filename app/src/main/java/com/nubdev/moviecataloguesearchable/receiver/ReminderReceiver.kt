package com.nubdev.moviecataloguesearchable.receiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.nubdev.moviecataloguesearchable.BuildConfig
import com.nubdev.moviecataloguesearchable.R
import com.nubdev.moviecataloguesearchable.activity.BottomNavigation
import java.text.SimpleDateFormat
import java.util.*


class ReminderReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_MESSAGE = "message"
        const val EXTRA_TYPE = "type"
        const val EXTRA_DATE = "date"
        const val ID_REPEATING = 101
        const val ID_NEW_MOVIES = 102
        private const val API_KEY = BuildConfig.TMDB_API_KEY
    }

    override fun onReceive(context: Context, intent: Intent) {
        val message = context.getString(R.string.daily_reminder_message)
        val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Calendar.getInstance().getTime())
        val type = intent.getStringExtra(EXTRA_TYPE)
        val title = context.getString(R.string.daily_reminder_title)

        Log.d("type", type)

        if (type == "daily") {
            showAlarmNotification(context, title, message,
                ID_REPEATING
            )
        } else {
            Log.d("type", date)
            getNewMoviesData(context, date.toString())
        }
    }

    private fun getNewMoviesData(context: Context, date: String?) {
        val url = "https://api.themoviedb.org/3/discover/movie?api_key=$API_KEY&primary_release_date.gte=$date&primary_release_date.lte=$date"
        Log.d("type", "https://api.themoviedb.org/3/discover/movie?api_key=$API_KEY&primary_release_date.gte=$date&primary_release_date.lte=$date")
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener { response ->
                val jsonArray = response.getJSONArray("results")
                for (i in 0 until jsonArray.length()) {
                    val movie = jsonArray.getJSONObject(i)
                    val title = movie.getString("original_title")
                    val message = title + context.getString(R.string.movie_release_message)
                    val notifId : Int = Integer.parseInt(SimpleDateFormat("yyMMddssSS", Locale.getDefault()).format(Calendar.getInstance().getTime()))
                    val addTitle = context.getString(R.string.movie_release_title_add)
                    showAlarmNotification(context, addTitle + title , message,
                        notifId
                    )

                    Log.d("type", message)
                }
            },

            Response.ErrorListener { error ->
            })

        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(jsonObjectRequest)
    }

    private fun showAlarmNotification(context: Context, title: String, message: String, notifId: Int) {
        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "AlarmManager channel"

        val contentIntent : Intent = Intent(context, BottomNavigation::class.java)
        val contentPendingIntent : PendingIntent = PendingIntent.getActivity(
            context, ID_REPEATING, contentIntent, PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(com.nubdev.moviecataloguesearchable.R.drawable.ic_local_movies)
            .setContentTitle(title)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.transparent))
            .setContentIntent(contentPendingIntent)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)

            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)

            builder.setChannelId(CHANNEL_ID)

            notificationManagerCompat.createNotificationChannel(channel)
        }

        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }
}
