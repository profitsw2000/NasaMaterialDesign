package ru.profitsw2000.nasamaterialdesign.ui.fragments.viewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.tabs.TabLayoutMediator
import ru.profitsw2000.nasamaterialdesign.R
import ru.profitsw2000.nasamaterialdesign.databinding.ActivityViewPagerBinding
import ru.profitsw2000.nasamaterialdesign.ui.adapters.EARTH_KEY
import ru.profitsw2000.nasamaterialdesign.ui.adapters.EPIC_KEY
import ru.profitsw2000.nasamaterialdesign.ui.adapters.MARS_KEY
import ru.profitsw2000.nasamaterialdesign.ui.adapters.ViewPagerAdapter

class ViewPagerActivity : AppCompatActivity() {

    lateinit var binding: ActivityViewPagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityViewPagerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.viewPager.adapter = ViewPagerAdapter(this)

        TabLayoutMediator(binding.tabLayout,binding.viewPager
        ) { tab, position ->
            tab.text = when (position) {
                EARTH_KEY -> "Earth"
                MARS_KEY -> "Mars"
                EPIC_KEY -> "EPIC"
                else -> "Earth"
            }
        }.attach()

        binding.tabLayout.getTabAt(EARTH_KEY)?.setIcon(R.drawable.icons_earth)
        binding.tabLayout.getTabAt(MARS_KEY)?.setIcon(R.drawable.icon_mars)
        binding.tabLayout.getTabAt(EPIC_KEY)?.setIcon(R.drawable.icon_epic_earth)
    }
}