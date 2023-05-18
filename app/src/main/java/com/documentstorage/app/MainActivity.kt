package com.documentstorage.app

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.documentstorage.app.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val auth = FirebaseAuth.getInstance()
    private val filesFragment = FilesFragment()
    private val cloudFragment = CloudFragment()
    private val profileFragment = ProfileFragment()
    private val addFragment = AddFragment()
    private val tutorialFragment = TutorialFragment()

  private val permissionCode = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        checkAuth()
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(filesFragment)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {

            when(it.itemId) {
                R.id.files -> replaceFragment(filesFragment)
                R.id.cloud -> replaceFragment(cloudFragment)
                R.id.profile -> replaceFragment(profileFragment)
                R.id.add -> replaceFragment(addFragment)
                R.id.questions -> replaceFragment(tutorialFragment)

                else -> replaceFragment(filesFragment)
            }
            true
        }

        if (checkPermissions())
            requestPermission()
    }

    override fun onStop() {
        super.onStop()
        scheduleNotificationAlarm(this)
    }

    private fun checkAuth() {
        val currentUser = auth.currentUser

        if (currentUser == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun checkPermissions(): Boolean {
        val writeStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            WRITE_EXTERNAL_STORAGE
        )

        val readStoragePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            READ_EXTERNAL_STORAGE
        )

        return writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE), permissionCode
        )
    }
    private fun replaceFragment(fragment: Fragment) {

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }

    // Schedule the alarm to trigger after 24 hours
    @SuppressLint("UnspecifiedImmutableFlag")
    fun scheduleNotificationAlarm(context: Context) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val notificationIntent = Intent(context, NotificationReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        // Calculate the alarm time: 24 hours from the current time
        val alarmTime = Calendar.getInstance().apply {
            add(Calendar.MINUTE, 1)
        }

        // Set the alarm to trigger after 24 hours
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime.timeInMillis, pendingIntent)
    }
}