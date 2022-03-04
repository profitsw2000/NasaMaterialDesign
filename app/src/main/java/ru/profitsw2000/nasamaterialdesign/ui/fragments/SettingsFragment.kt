package ru.profitsw2000.nasamaterialdesign.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import ru.profitsw2000.nasamaterialdesign.R
import ru.profitsw2000.nasamaterialdesign.databinding.FragmentSettingsBinding
import ru.profitsw2000.nasamaterialdesign.ui.*

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val context: Context = ContextThemeWrapper(activity, getRealStyle(getCurrentTheme()))
        val localInflater = inflater.cloneInContext(context)
        _binding = FragmentSettingsBinding.inflate(localInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //установить чип в зависимости от текущей темы
        //setChipFromChipGroup(getCurrentTheme())

        //слушатель нажатия на чип
        binding?.settingsChipGroup?.setOnCheckedChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let {
                setThemeWithSelectedChip(it.text.toString())
                Toast.makeText(requireContext(),it.text.toString(),Toast.LENGTH_SHORT).show()
            }
/*            val manager = activity?.supportFragmentManager
            manager?.let { manager ->
                manager.beginTransaction()
                    .detach(SettingsFragment.newInstance())
                    .attach(SettingsFragment.newInstance())
                    .commit()
            }*/
        }
    }

    private fun setChipFromChipGroup(currentTheme: Int) {
        when(getCurrentTheme()){
            CosmicTheme -> {
                binding?.settingsChipGroup?.check(binding?.cosmic?.id!!)
            }

            MoonTheme -> {
                binding?.settingsChipGroup?.check(binding?.moon?.id!!)
            }

            MarsTheme -> {
                binding?.settingsChipGroup?.check(binding?.mars?.id!!)
            }

            else -> {

            }
        }
    }

    private fun setThemeWithSelectedChip(theme: String) {
        when(theme){
            resources.getString(R.string.cosmic_theme_chip) -> {
                setCurrentTheme(CosmicTheme)
            }

            resources.getString(R.string.moon_theme_chip) -> {
                setCurrentTheme(MoonTheme)
            }

            resources.getString(R.string.mars_theme_chip) -> {
                setCurrentTheme(MarsTheme)
            }

            else -> {

            }
        }

    }

    private fun setCurrentTheme(currentTheme: Int) {
        val sharedPreferences = requireActivity().getSharedPreferences(KEY_SP, AppCompatActivity.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_CURRENT_THEME, currentTheme)
        editor.apply()
    }

    private fun getCurrentTheme(): Int {
        val sharedPreferences = requireActivity().getSharedPreferences(KEY_SP, AppCompatActivity.MODE_PRIVATE)
        return sharedPreferences.getInt(KEY_CURRENT_THEME, -1)
    }

    private fun getRealStyle(currentTheme: Int): Int {
        return when (currentTheme) {
            CosmicTheme -> R.style.Cosmic
            MoonTheme -> R.style.Moon
            MarsTheme -> R.style.Mars
            else -> 0
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = SettingsFragment()
    }
}