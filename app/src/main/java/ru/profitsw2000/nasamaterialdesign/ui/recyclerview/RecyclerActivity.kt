package ru.profitsw2000.nasamaterialdesign.ui.recyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import ru.profitsw2000.nasamaterialdesign.R
import ru.profitsw2000.nasamaterialdesign.databinding.ActivityRecyclerBinding
import ru.profitsw2000.nasamaterialdesign.ui.*

class RecyclerActivity : AppCompatActivity() {

    lateinit var binding: ActivityRecyclerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(getRealStyle(getCurrentTheme()))
        setContentView(R.layout.activity_recycler)
        binding = ActivityRecyclerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun getCurrentTheme(): Int {
        val sharedPreferences = getSharedPreferences(KEY_SP, MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_CURRENT_THEME, -1)
    }

    fun getRealStyle(currentTheme: Int): Int {
        return when (currentTheme) {
            CosmicTheme -> R.style.Cosmic
            MoonTheme -> R.style.Moon
            MarsTheme -> R.style.Mars
            else -> 0
        }
    }
}