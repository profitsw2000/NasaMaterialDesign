package ru.profitsw2000.nasamaterialdesign.ui.fragments.navigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.profitsw2000.nasamaterialdesign.R
import ru.profitsw2000.nasamaterialdesign.databinding.ActivityBottomNavigationBinding

class BottomNavigationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBottomNavigationBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBottomNavigationBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_bottom_navigation)
    }
}