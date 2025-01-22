package com.example.appmovel_pis.ui.objects

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView

object WelcomePageAnimations {

    // Logo Animation: Zoom-In
    fun zoomIn(view: View, duration: Long = 500) {
        view.scaleX = 0f
        view.scaleY = 0f
        view.animate()
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(duration)
            .setInterpolator(AccelerateInterpolator())
            .start()
    }

    // Button Slide-Up and Fade-In Animation
    fun slideUpAndFadeIn(view: View, duration: Long = 800) {
        view.translationY = 200f
        view.alpha = 0f
        view.animate()
            .translationY(0f)
            .alpha(1f)
            .setDuration(duration)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }

    // Text Typewriter Effect
    fun typewriterEffect(textView: TextView, text: String, delayPerChar: Long = 50) {
        textView.text = ""
        for (i in text.indices) {
            textView.postDelayed({
                textView.text = text.substring(0, i + 1)
            }, i * delayPerChar)
        }
    }

    // Fade-In Animation
    fun fadeIn(view: View, duration: Long = 1000) {
        view.alpha = 0f
        view.animate()
            .alpha(1f)
            .setDuration(duration)
            .setInterpolator(AccelerateDecelerateInterpolator())
            .start()
    }
}