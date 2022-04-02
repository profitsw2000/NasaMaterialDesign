package ru.profitsw2000.nasamaterialdesign.ui.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.chip.Chip
import ru.profitsw2000.nasamaterialdesign.R
import ru.profitsw2000.nasamaterialdesign.databinding.FragmentSettingsBinding
import ru.profitsw2000.nasamaterialdesign.ui.*

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private var state = false

    private lateinit var parentActivity: MainActivity
    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivity = (context as MainActivity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSettingsBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //установить чип в зависимости от текущей темы
        setChipFromChipGroup(parentActivity.getCurrentTheme())
    }

    override fun onResume() {
        super.onResume()
        //слушатель нажатия на чип
        binding.settingsChipGroup.setOnCheckedChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let {
                setThemeWithSelectedChip(it.text.toString())
            }
        }
    }

    private fun setChipFromChipGroup(currentTheme: Int) {
        when(currentTheme){
            CosmicTheme -> {
                binding.settingsChipGroup.check(binding.cosmic.id)
            }

            MoonTheme -> {
                binding.settingsChipGroup.check(binding.moon.id)
            }

            MarsTheme -> {
                binding.settingsChipGroup.check(binding.mars.id!!)
            }

            else -> {

            }
        }
    }

    private fun setThemeWithSelectedChip(theme: String) {
        when(theme){
            resources.getString(R.string.cosmic_theme_chip) -> {
                parentActivity.setCurrentTheme(CosmicTheme)
                parentActivity.recreate()
            }

            resources.getString(R.string.moon_theme_chip) -> {
                parentActivity.setCurrentTheme(MoonTheme)
                parentActivity.recreate()
            }

            resources.getString(R.string.mars_theme_chip) -> {
                parentActivity.setCurrentTheme(MarsTheme)
                parentActivity.recreate()
            }

            else -> {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}