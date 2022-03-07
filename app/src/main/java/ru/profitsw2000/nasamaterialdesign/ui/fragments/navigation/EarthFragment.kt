package ru.profitsw2000.nasamaterialdesign.ui.fragments.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.api.load
import ru.profitsw2000.nasamaterialdesign.databinding.FragmentEarthBinding

class EarthFragment : Fragment() {

    private var _binding: FragmentEarthBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEarthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding){
            imageView.load("https://api.nasa.gov/planetary/earth/imagery?lon=30.523333&lat=50.450001&date=2021-03-07&dim=0.25&api_key=Mg2woVFc6hKt5xjKlvUKn98AGStgNooNb2AGolcN")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = EarthFragment()
    }
}