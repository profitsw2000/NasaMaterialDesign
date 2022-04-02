package ru.profitsw2000.nasamaterialdesign.ui.fragments

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.*
import android.view.*
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.transition.ChangeBounds
import androidx.transition.ChangeImageTransform
import androidx.transition.TransitionManager
import androidx.transition.TransitionSet
import coil.api.load
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import ru.profitsw2000.nasamaterialdesign.R
import ru.profitsw2000.nasamaterialdesign.model.PictureOfTheDayViewModel
import ru.profitsw2000.nasamaterialdesign.databinding.FragmentMainBinding
import ru.profitsw2000.nasamaterialdesign.model.PictureOfTheDayData
import ru.profitsw2000.nasamaterialdesign.ui.MainActivity
import ru.profitsw2000.nasamaterialdesign.ui.utils.DoubleClickListener
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern

class PictureOfTheDayFragment : Fragment() {


    private val viewModel: PictureOfTheDayViewModel by lazy {
        ViewModelProvider(this).get(PictureOfTheDayViewModel::class.java)
    }

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    var isMain:Boolean = true
    private var flag = false
    private var doubleClick = false
    private val duration = 1000L
    private var zoomFlag = false

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
                binding.mainGroup.hide()
                expandFAB()
            }else{
                collapseFAB()
                binding.mainGroup.show()
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
        binding.imageView.setOnClickListener(object: DoubleClickListener(){
            override fun onDoubleClick() {
                doubleClick = true
                zoomFlag = !zoomFlag
                zoomImage()
            }

            override fun onSingleClick() {
                if (!doubleClick) {
                    flag = !flag
                    val constraintSet= ConstraintSet()
                    constraintSet.clone(binding.main)
                    if (flag) {
                        showPictureTitle(constraintSet)
                    } else {
                        hidePictureTitle(constraintSet)
                    }
                }
                doubleClick = false
            }

        })

        //слушатель нажатия на текст "Показать фото дня"
        binding.showPictureOfTheDayText.setOnClickListener {
            imageAnimation()
        }
    }

    private fun zoomImage() {
        val changeBounds = ChangeImageTransform()
        changeBounds.duration = 2000
        TransitionManager.beginDelayedTransition(binding.main, changeBounds)
        val params: ViewGroup.LayoutParams = binding.imageView.layoutParams
        params.height =
            if (zoomFlag) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
        params.width =
            if (zoomFlag) ViewGroup.LayoutParams.MATCH_PARENT else ViewGroup.LayoutParams.WRAP_CONTENT
        binding.imageView.layoutParams = params
        binding.imageView.scaleType =
            if (zoomFlag) ImageView.ScaleType.CENTER_CROP else ImageView.ScaleType.FIT_CENTER
    }

    private fun showPictureTitle(constraintSet: ConstraintSet) {
        constraintSet.clone(binding.main)
        val changeBounds = ChangeBounds()
        changeBounds.interpolator = AnticipateOvershootInterpolator(2.0f)
        changeBounds.duration= 1000L
        TransitionManager.beginDelayedTransition(binding.main,changeBounds)

        constraintSet.connect(R.id.title,ConstraintSet.END, R.id.main,ConstraintSet.END)
        constraintSet.connect(R.id.title,ConstraintSet.START, R.id.main,ConstraintSet.START)
        constraintSet.setHorizontalBias(R.id.title,0.8f)
        constraintSet.constrainPercentWidth(R.id.title,0.5f)
        constraintSet.applyTo(binding.main)
    }

    private fun hidePictureTitle(constraintSet: ConstraintSet) {
        constraintSet.clone(binding.main)
        val changeBounds = ChangeBounds()
        changeBounds.interpolator = AnticipateOvershootInterpolator(2.0f)
        changeBounds.duration= 1000L
        TransitionManager.beginDelayedTransition(binding.main,changeBounds)

        constraintSet.connect(R.id.title,ConstraintSet.END, R.id.main,ConstraintSet.START)
        constraintSet.clear(R.id.title,ConstraintSet.START)
        constraintSet.applyTo(binding.main)
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
                        if (data.serverResponseData.mediaType == "video") {
                            binding.ytPlayer.visibility = View.VISIBLE
                            binding.imageView.visibility = View.GONE
                            showNasaVideo(extractYTId(url))
                        } else {
                            binding.ytPlayer.visibility = View.GONE
                            binding.imageView.visibility = View.VISIBLE
                            binding.imageView.load(data.serverResponseData.hdurl)
                        }

                        title.setText(data.serverResponseData.title)
                        date.setText(data.serverResponseData.date)

                        if (data.serverResponseData.title != null && data.serverResponseData.explanation != null)
                        setSpannedText(data.serverResponseData.title, data.serverResponseData.explanation)
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

    private fun setSpannedText(title: String, text: String){
        //Заглавие
        val titleSpannableString = SpannableString(title)
        val gap = 100
        val color = ContextCompat.getColor(requireContext(), R.color.color_red)
        val bulletRadius = 20
        val fontSizeMultiplier = 2.0f
        val fontScaleMultiplier = 1.5f
        //описание фото дня
        val lineHeight = 75
        var spannableDescription  = SpannableStringBuilder(text)

        //Изменение заголовка
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            titleSpannableString.setSpan(BulletSpan(gap, color, bulletRadius), 0, title.length, 0)
        } else {
            titleSpannableString.setSpan(BulletSpan(gap, color), 0, title.length, 0)
        }
        titleSpannableString.setSpan(RelativeSizeSpan(fontSizeMultiplier), 0, title.length, 0)
        titleSpannableString.setSpan(ScaleXSpan(fontScaleMultiplier), 0, title.length, 0)
        binding.included.bottomSheetDescriptionHeader.text = titleSpannableString

        //работа с текстом описания к фото дня
        binding.included.bottomSheetDescription.setText(spannableDescription, TextView.BufferType.EDITABLE)
        spannableDescription = binding.included.bottomSheetDescription.text as SpannableStringBuilder

        //установка высоты строки
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            spannableDescription.setSpan(LineHeightSpan.Standard(lineHeight), 0, spannableDescription.length, 0)
        }

        //текст делится на предложения, каждое предложение со своим стилем
        //стиль предложения повторяется через каждые 2 предложения
        var fromIndex = 0
        var pointCounter = 0
        var toIndex = spannableDescription.indexOf('.', fromIndex)
        while (toIndex != -1) {
            pointCounter++
            when(pointCounter%3) {
                1 -> {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        spannableDescription.setSpan(StyleSpan(Typeface.BOLD_ITALIC), fromIndex, toIndex, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE)
                        val fontRes = ResourcesCompat.getFont(requireContext(), R.font.bonbon)
                        val typeface = Typeface.create(fontRes, Typeface.BOLD)
                        spannableDescription.setSpan(TypefaceSpan(typeface), fromIndex, toIndex, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE)
                        val bkgColor = ContextCompat.getColor(requireContext(), R.color.color_brown_600)
                        spannableDescription.setSpan(BackgroundColorSpan(bkgColor), fromIndex, toIndex, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE)
                        val frgColor = ContextCompat.getColor(requireContext(), R.color.primaryColorMoon)
                        spannableDescription.setSpan(ForegroundColorSpan(frgColor), fromIndex, toIndex, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE)
                        val stripeColor = ContextCompat.getColor(requireContext(), R.color.color_green)
                        val stripeWidth = 40
                        val gapWidth = 100
                        spannableDescription.setSpan(QuoteSpan(stripeColor, stripeWidth, gapWidth), fromIndex, toIndex + 1, 0)
                    }
                }
                2 -> {
                    spannableDescription.setSpan(UnderlineSpan(), fromIndex + 1, toIndex, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE)
                }
                else -> {
                    spannableDescription.setSpan(StrikethroughSpan(), fromIndex + 1, toIndex, SpannableString.SPAN_EXCLUSIVE_INCLUSIVE)
                }
            }
            val verticalAlignment = DynamicDrawableSpan.ALIGN_BASELINE
            val drawable = ContextCompat.getDrawable(requireContext(), R.drawable.icons_smile)!!

            spannableDescription.insert(toIndex + 1,"\n")
            drawable.setBounds(0,0,40,40)
            spannableDescription.setSpan(ImageSpan(drawable, verticalAlignment), toIndex,toIndex + 1,0)

            fromIndex = toIndex + 1
            toIndex = spannableDescription.indexOf('.', fromIndex)
        }
    }


    private fun showNasaVideo(videoId: String) {
        with(binding) {
            lifecycle.addObserver(binding.ytPlayer)

            ytPlayer.addYouTubePlayerListener(object :
                AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.cueVideo(videoId, 0f)
                }
            })
        }
    }

    fun extractYTId(ytUrl: String): String {
        var vId: String? = null
        val pattern = Pattern.compile(
            "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
            Pattern.CASE_INSENSITIVE
        )
        val matcher = pattern.matcher(ytUrl)
        if (matcher.matches()) {
            vId = matcher.group(1)
        }
        return vId!!
    }

    private fun imageAnimation() {

        val constraintLayout = binding.main
        val constraintSet = ConstraintSet()
        constraintSet.clone(constraintLayout)
        constraintSet.clear(R.id.imageView, ConstraintSet.START)
        constraintSet.clear(R.id.show_picture_of_the_day_text, ConstraintSet.START)
        constraintSet.connect(R.id.imageView, ConstraintSet.START, R.id.main, ConstraintSet.START, 0)
        constraintSet.connect(R.id.imageView, ConstraintSet.END, R.id.main, ConstraintSet.END, 0)
        constraintSet.connect(R.id.show_picture_of_the_day_text, ConstraintSet.END, R.id.main, ConstraintSet.START, 0)
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
            R.id.app_bar_fav -> {
                bottomSheetBehavior = BottomSheetBehavior.from(binding.included.bottomSheet)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
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

    private fun expandFAB() {
        ObjectAnimator.ofFloat( binding.fab,View.ROTATION,0f,405f).setDuration(duration).start()
        ObjectAnimator.ofFloat( binding.optionOneContainer,View.TRANSLATION_Y,-50f,-260f).setDuration(duration).start()
        ObjectAnimator.ofFloat( binding.optionTwoContainer,View.TRANSLATION_Y,-20f,-130f).setDuration(duration).start()

        binding.optionOneContainer.animate()
            .alpha(1f)
            .setDuration(duration/2)
            .setListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    binding.optionOneContainer.isClickable = true
                }
            })
        binding.optionTwoContainer.animate()
            .alpha(1f)
            .setDuration(duration/2)
            .setListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    binding.optionTwoContainer.isClickable = true
                }
            })

        binding.transparentBackground.animate()
            .alpha(0.5f)
            .setDuration(duration)

    }

    private fun collapseFAB() {
        ObjectAnimator.ofFloat( binding.fab,View.ROTATION,405f,0f).setDuration(duration).start()
        ObjectAnimator.ofFloat( binding.optionOneContainer,View.TRANSLATION_Y,-260f,-50f).setDuration(duration).start()
        ObjectAnimator.ofFloat( binding.optionTwoContainer,View.TRANSLATION_Y,-130f,-20f).setDuration(duration).start()

        binding.optionOneContainer.animate()
            .alpha(0f)
            .setDuration(duration/2)
            .setListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    binding.optionOneContainer.isClickable = false
                }
            })
        binding.optionTwoContainer.animate()
            .alpha(0f)
            .setDuration(duration/2)
            .setListener(object : AnimatorListenerAdapter(){
                override fun onAnimationEnd(animation: Animator?) {
                    super.onAnimationEnd(animation)
                    binding.optionTwoContainer.isClickable = false
                }
            })
        binding.transparentBackground.animate()
            .alpha(0f)
            .setDuration(duration)
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
