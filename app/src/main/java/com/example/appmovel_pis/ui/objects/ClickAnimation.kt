package com.example.appmovel_pis.ui.objects

import android.content.Context
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import com.example.appmovel_pis.R

object ClickAnimation {
    fun applyTouchAnimation(view: View, context: Context) {
        view.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val pressAnimation = AnimationUtils.loadAnimation(context, R.anim.button_press)
                    v.startAnimation(pressAnimation)
                }
                MotionEvent.ACTION_UP -> {
                    val releaseAnimation = AnimationUtils.loadAnimation(context, R.anim.button_release)
                    v.startAnimation(releaseAnimation)
                    v.performClick()
                }
            }
            true
        }
    }
}