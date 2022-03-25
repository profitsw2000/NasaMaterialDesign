package ru.profitsw2000.nasamaterialdesign.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.profitsw2000.nasamaterialdesign.R
import ru.profitsw2000.nasamaterialdesign.databinding.BottomNavigationLayoutBinding
import ru.profitsw2000.nasamaterialdesign.ui.fragments.navigation.BottomNavigationActivity
import ru.profitsw2000.nasamaterialdesign.ui.fragments.viewpager.ViewPagerActivity

class BottomNavigationDrawerFragment : BottomSheetDialogFragment() {
    private var _binding: BottomNavigationLayoutBinding? = null
    private val binding: BottomNavigationLayoutBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomNavigationLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.navigation_one->{
                    startActivity(Intent(requireContext(),ViewPagerActivity::class.java))
                }
                R.id.navigation_two->{
                    startActivity(Intent(requireContext(), BottomNavigationActivity::class.java))
                }
            }
            true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}