package com.example.androidsecondsem.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.androidsecondsem.R
import com.example.androidsecondsem.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(savedInstanceState !=null) {
            return
        }
        supportFragmentManager.beginTransaction()
            .add(
                R.id.container_of_fragments,
                SearchFragment.newInstance(Bundle()),
                SearchFragment.SEARCH_FRAGMENT_TAG
            ).commit()
    }
}
