package ru.profitsw2000.nasamaterialdesign.ui.utils

import android.os.Handler
import android.os.SystemClock
import android.view.View

abstract class DoubleClickListener() : View.OnClickListener {

    private val delta: Long
    private var deltaClick: Long

    override fun onClick(v: View) {
        val handler = Handler()
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({ onSingleClick() }, delta)
        if (SystemClock.elapsedRealtime() - deltaClick < delta) {
            handler.removeCallbacksAndMessages(null)
            onDoubleClick()
        }
        deltaClick = SystemClock.elapsedRealtime()
    }

    abstract fun onDoubleClick()
    abstract fun onSingleClick()

    companion object {
        private const val DEFAULT_QUALIFICATION_SPAN: Long = 300
    }

    init {
        delta = DEFAULT_QUALIFICATION_SPAN
        deltaClick = 0
    }
}