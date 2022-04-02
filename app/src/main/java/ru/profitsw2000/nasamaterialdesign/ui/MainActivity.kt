package ru.profitsw2000.nasamaterialdesign.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import ru.profitsw2000.nasamaterialdesign.R
import ru.profitsw2000.nasamaterialdesign.ui.fragments.PictureOfTheDayFragment

const val CosmicTheme = 0
const val MoonTheme = 1
const val MarsTheme = 2
val KEY_SP = "Key sp"
val KEY_CURRENT_THEME = "Current theme"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //setTheme(R.style.MySplashTheme)
        setTheme(getRealStyle(getCurrentTheme()))

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commitNow()
        }
        setContentView(R.layout.activity_main)
    }

    private fun openFragment(fragment: Fragment) {
        supportFragmentManager.apply {
            beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
    }

    fun setCurrentTheme(currentTheme: Int) {
        val sharedPreferences = getSharedPreferences(KEY_SP, MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_CURRENT_THEME, currentTheme)
        editor.apply()
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