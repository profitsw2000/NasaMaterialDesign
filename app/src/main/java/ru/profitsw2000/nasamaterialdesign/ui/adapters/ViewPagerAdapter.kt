package ru.profitsw2000.nasamaterialdesign.ui.adapters

import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.profitsw2000.nasamaterialdesign.ui.fragments.navigation.EPICFragment
import ru.profitsw2000.nasamaterialdesign.ui.fragments.navigation.EarthFragment
import ru.profitsw2000.nasamaterialdesign.ui.fragments.navigation.MarsFragment

const val EARTH_KEY = 0
const val MARS_KEY = 1
const val EPIC_KEY = 2

class ViewPagerAdapter(private val fragmentManager: FragmentActivity) :
    FragmentStateAdapter(fragmentManager) {

    private val fragments = arrayOf(EarthFragment(), MarsFragment(), EPICFragment())


    override fun getItemCount() = fragments.size

    override fun createFragment(position: Int)= fragments[position]

}