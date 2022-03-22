package ru.profitsw2000.nasamaterialdesign.ui.fragments

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import coil.api.load
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import ru.profitsw2000.nasamaterialdesign.R
import ru.profitsw2000.nasamaterialdesign.model.PictureOfTheDayViewModel
import ru.profitsw2000.nasamaterialdesign.databinding.FragmentMainBinding
import ru.profitsw2000.nasamaterialdesign.model.PictureOfTheDayData
import ru.profitsw2000.nasamaterialdesign.ui.MainActivity
import java.text.SimpleDateFormat
import java.util.*

class PictureOfTheDayFragment : Fragment() {


    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    var isMain:Boolean = true
    private var flag = false

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
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED

        //добавление BottomBar
        setBottomAppBar(view)

        binding.fab.setOnClickListener{
            if(isMain){
                with(binding){
                    bottomAppBar.navigationIcon = null
                    bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_END
                    fab.setImageDrawable(ContextCompat.getDrawable(requireContext(),R.drawable.ic_back_fab))
                    bottomAppBar.replaceMenu(R.menu.menu_bottom_bar_other_screen)
                }
            }else{
                with(binding) {
                    bottomAppBar.navigationIcon = ContextCompat.getDrawable(
                        requireContext(),
                        R.drawable.ic_hamburger_menu_bottom_bar
                    )
                    bottomAppBar.fabAlignmentMode = BottomAppBar.FAB_ALIGNMENT_MODE_CENTER
                    fab.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            R.drawable.ic_plus_fab
                        )
                    )
                    bottomAppBar.replaceMenu(R.menu.menu_bottom_bar)
                }
            }
            isMain = !isMain
        }

        //слушатель нажатия на чип
        binding.chipGroup.setOnCheckedChangeListener { chipGroup, position ->
            chipGroup.findViewById<Chip>(position)?.let {
                setImageWithSelectedChip(it.text.toString())
            }
        }

        //слушатель нажатия на картинку
        binding.imageView.setOnClickListener {
            val changeBounds = ChangeImageTransform()
            changeBounds.duration = 3000
            TransitionManager.beginDelayedTransition(binding.main,changeBounds)
            flag = !flag
            val params: ViewGroup.LayoutParams = binding.imageView.layoutParams
            params.height = if (flag) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
            params.width = if (flag) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
            binding.imageView.layoutParams = params
            binding.imageView.scaleType = if(flag) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
        }
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
                        progressBar.hide()
                        included.bottomSheet.show()
                        imageView.load(data.serverResponseData.hdurl)
                        imageAnimation()
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

    private fun imageAnimation() {

        val constraintLayout = binding.main
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.clear(R.id.imageView, ConstraintSet.START)
        constraintSet.connect(R.id.imageView, ConstraintSet.START, R.id.main, ConstraintSet.START, 0)
        val transition = TransitionSet()
        val changeBounds = ChangeBounds()
        changeBounds.duration = 2000
        transition.addTransition(changeBounds)
        TransitionManager.beginDelayedTransition(binding.main, transition)
        constraintSet.applyTo(constraintLayout)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_bottom_bar,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.app_bar_fav -> Toast.makeText(requireContext(),"Favourite",Toast.LENGTH_SHORT).show()
            R.id.app_bar_settings -> openSettingsFragment()
            android.R.id.home -> {
                BottomNavigationDrawerFragment().show(requireActivity().supportFragmentManager,"")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBottomAppBar(view: View) {
        val context = activity as MainActivity
        context.setSupportActionBar(view.findViewById(R.id.bottom_app_bar))
        setHasOptionsMenu(true)
    }

    private fun setImageWithSelectedChip(dayOfWeek: String) {

        when(dayOfWeek){
            resources.getString(R.string.today_chip) -> {
                val calendar = Calendar.getInstance()
                val currentDay = calendar[Calendar.DATE]
                val dateCurrentDay: String = SimpleDateFormat("yyyy-MM-$currentDay", Locale.getDefault()).format(Date())
                val observer = Observer<PictureOfTheDayData> { renderData(it) }
                viewModel.getData(dateCurrentDay).observe(viewLifecycleOwner, observer)
            }

            resources.getString(R.string.yesterday_chip) -> {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DATE,-1)
                val dateYesterday: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                val observer = Observer<PictureOfTheDayData> { renderData(it) }
                viewModel.getData(dateYesterday).observe(viewLifecycleOwner, observer)
            }

            resources.getString(R.string.before_yesterday_chip) -> {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.DATE,-2)
                val dateBeforeYesterday: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
                val observer = Observer<PictureOfTheDayData> { renderData(it) }
                viewModel.getData(dateBeforeYesterday).observe(viewLifecycleOwner, observer)
            }
            else -> {

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun openSettingsFragment() {
        val manager = activity?.supportFragmentManager
        manager?.let { manager ->
                manager.beginTransaction()
                .replace(R.id.container, SettingsFragment.newInstance())
                .addToBackStack("")
                .commitAllowingStateLoss()
        }
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
/*val behavior = BottomSheetBehavior.from(binding.includeBottomSheet.bottomSheetContainer)
if (isMain) {
    isMain = false
    behavior.state = BottomSheetBehavior.STATE_EXPANDED
    ...
} else {
    isMain = true
    behavior.state = BottomSheetBehavior.STATE_HIDDEN
    ...
}*/
