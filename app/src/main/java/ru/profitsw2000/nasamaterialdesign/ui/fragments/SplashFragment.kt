package ru.profitsw2000.nasamaterialdesign.ui.fragments

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import ru.profitsw2000.nasamaterialdesign.R
import ru.profitsw2000.nasamaterialdesign.databinding.FragmentSettingsBinding
import ru.profitsw2000.nasamaterialdesign.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler(requireContext().mainLooper).postDelayed({
            openMainFragment()

        }, 3000L)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSplashBinding.inflate(inflater)
        return binding?.root
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun openMainFragment() {
        val manager = activity?.supportFragmentManager
        manager?.let { manager ->
            manager.beginTransaction()
                .replace(R.id.container, PictureOfTheDayFragment.newInstance())
                .commitAllowingStateLoss()
        }
    }

    companion object {
        fun newInstance() = SplashFragment()
    }
}