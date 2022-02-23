package ru.profitsw2000.nasamaterialdesign.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import ru.profitsw2000.nasamaterialdesign.R
import ru.profitsw2000.nasamaterialdesign.model.PictureOfTheDayViewModel
import ru.profitsw2000.nasamaterialdesign.databinding.FragmentMainBinding
import ru.profitsw2000.nasamaterialdesign.model.PictureOfTheDayData

class PictureOfTheDayFragment : Fragment() {


    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val observer = Observer<PictureOfTheDayData> { renderData(it) }
        viewModel.getData().observe(viewLifecycleOwner, observer)

        //обработчик нажатия на иконку википедии в строке поиска
        binding.inputLayout.setEndIconOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://en.wikipedia.org/wiki/${binding.inputEditText.text.toString()}")
            })
        }

        //добавление Bottom Sheet во фрагмент
        bottomSheetBehavior = BottomSheetBehavior.from(binding.included.bottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun renderData(data: PictureOfTheDayData) {
        when (data) {
            is PictureOfTheDayData.Success -> {
                val serverResponseData = data.serverResponseData
                val url = serverResponseData.url
                with(binding) {
                    progressBar.hide()
                    included.bottomSheet.hide()
                    mainGroup.hide()
                }
                if (url.isNullOrEmpty()) {
                    showDialog(getString(R.string.title_url_empty_text), getString(R.string.message_url_empty_text))
                } else {
                    with(binding) {
                        mainGroup.show()
                        included.bottomSheet.show()
                        imageView.load(data.serverResponseData.hdurl)
                        included.bottomSheetDescriptionHeader.text = data.serverResponseData.title
                        included.bottomSheetDescription.text = data.serverResponseData.explanation
                    }
                }
            }
            is PictureOfTheDayData.Loading -> {
                with(binding){
                    progressBar.show()
                    mainGroup.hide()
                    included.bottomSheet.hide()
                }
            }
            is PictureOfTheDayData.Error -> {
                with(binding) {
                    progressBar.hide()
                    mainGroup.hide()
                    included.bottomSheet.hide()
                }
                showDialog("Error", data.error.message!!)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun View.showSnackBar (
        text: String,
        actionText: String,
        action: (View) -> Unit,
        length: Int = Snackbar.LENGTH_INDEFINITE
    ) {
        Snackbar.make(this, text, length).setAction(actionText, action).show()
    }

    private fun showDialog(title: String, message: String) {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(getString(R.string.dialog_button_reload_text)){ _, _ ->
                    val observer = Observer<PictureOfTheDayData> { renderData(it) }
                    viewModel.getData().observe(viewLifecycleOwner, observer)
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
        fun newInstance() = PictureOfTheDayFragment()
    }
}