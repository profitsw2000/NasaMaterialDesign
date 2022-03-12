package ru.profitsw2000.nasamaterialdesign.ui.fragments.behavior

import android.R.attr
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.google.android.material.appbar.AppBarLayout


class TitleBehavior(context: Context, attr: AttributeSet): CoordinatorLayout.Behavior<View>(context,attr)   {

    private var deltaY: Float = 0.0f

    override fun layoutDependsOn(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    )= dependency is TextView

    override fun onDependentViewChanged(
        parent: CoordinatorLayout,
        child: View,
        dependency: View
    ): Boolean {
        val tv = dependency as TextView

        if (deltaY.equals(0)) {
            deltaY = tv.y - child.height
        }

        var dy: Float = tv.y - child.height
        dy = if (dy < 0) 0f else dy
        val y: Float = -(dy / deltaY) * child.height
        child.translationY = y
        return super.onDependentViewChanged(parent, child, dependency)
    }
}