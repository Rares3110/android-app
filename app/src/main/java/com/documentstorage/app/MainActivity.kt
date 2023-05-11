package com.documentstorage.app

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.documentstorage.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Local())

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.local -> replaceFragment(Local())
                R.id.add -> replaceFragment(Add())

                else -> replaceFragment(Local())
            }
            true
        }
    }

    private fun replaceFragment(fragment : Fragment) {

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout, fragment)
        fragmentTransaction.commit()
    }
}