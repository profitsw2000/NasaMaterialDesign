package ru.profitsw2000.nasamaterialdesign.ui.fragments.navigation

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import ru.profitsw2000.nasamaterialdesign.R
import ru.profitsw2000.nasamaterialdesign.databinding.FragmentMarsBinding
import ru.profitsw2000.nasamaterialdesign.model.MEEViewModel
import ru.profitsw2000.nasamaterialdesign.model.PictureOfTheDayData
import java.text.SimpleDateFormat
import java.util.*

class MarsFragment : Fragment() {

    //ViewModel
    private val viewModel: MEEViewModel by lazy {
        ViewModelProvider(this).get(MEEViewModel::class.java)
    }
    //binding
    private var _binding: FragmentMarsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMarsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val observer = Observer<PictureOfTheDayData> { renderData(it) }
        viewModel.getMarsRoverData(getYesterdayDate()).observe(viewLifecycleOwner, observer)
/*        with(binding){
            imageView.load("https://mars.nasa.gov/msl-raw-images/proj/msl/redops/ods/surface/sol/01000/opgs/edr/fcam/FLB_486265257EDR_F0481570FHAZ00323M_.JPG")
        }*/
    }

    //Получение вчерашней даты.
    private fun getYesterdayDate(): String {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE,-3)
        return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
    }

    private fun renderData(data: PictureOfTheDayData?) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                with(binding) {
                    progressBar.hide()
                    imageView.show()
                }
                if (url.isNullOrEmpty()) {
                    showDialog(getString(R.string.title_url_empty_text), getString(R.string.message_url_empty_text))
                } else {
                    with(binding) {
                        imageView.load(data.serverResponseData.hdurl)
                    }
                }
            }
            is PictureOfTheDayData.Loading -> {
                with(binding){
                    progressBar.show()
                    imageView.hide()
                }
            }
            is PictureOfTheDayData.Error -> {
                with(binding) {
                    progressBar.hide()
                    imageView.hide()
                }
                showDialog("Error", data.error.message!!)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun showDialog(title: String, message: String) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.dialog_button_reload_text)){ _, _ ->
                    val observer = Observer<PictureOfTheDayData> { renderData(it) }
                    viewModel.getMarsRoverData(getYesterdayDate()).observe(viewLifecycleOwner, observer)
                }
                .setNegativeButton(getString(R.string.dialog_button_cancel_text)) {
                        dialog, _ -> dialog.dismiss() }
                .create()
                .show()
        }
    }

    private fun View.show() : View {
        if (visibility != View.VISIBLE) {
            visibility = View.VISIBLE
        }
        return this
    }

    private fun View.hide() : View {
        if (visibility != View.GONE) {
            visibility = View.GONE
        }
        return this
    }

    companion object {
        @JvmStatic
        fun newInstance() = MarsFragment()
    }
}