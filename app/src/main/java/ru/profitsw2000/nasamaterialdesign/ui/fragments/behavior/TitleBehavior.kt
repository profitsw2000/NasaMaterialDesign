package ru.profitsw2000.nasamaterialdesign.ui.fragments.behavior

import android.R.attr
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout
import java.lang.Math.abs


class TitleBehavior(context: Context, attr: AttributeSet): CoordinatorLayout.Behavior<View>(context,attr)   {

    private var dy: Float = 0.0f
    private var barInitHeight: Float = 0.0f

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    )= dependency is AppBarLayout

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val bar = dependency as AppBarLayout

        if (dy == 0.0f) {
            child.translationY = (-child.height).toFloat()
            barInitHeight = bar.height.toFloat()
        }

        dy = if(bar.height.toFloat() != barInitHeight) {
                (child.height/(bar.height.toFloat() - barInitHeight))
        } else {
            (child.height/(bar.height.toFloat()))
        }

        val deltaY = abs(dy*bar.y)
        child.translationY = deltaY - child.height.toFloat()

        return super.onDependentViewChanged(parent, child, dependency)
    }
}