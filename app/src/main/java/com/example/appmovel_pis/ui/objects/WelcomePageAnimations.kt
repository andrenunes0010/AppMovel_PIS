package com.example.appmovel_pis.ui.objects

import android.animation.ObjectAnimator
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import kotlinx.coroutines.Delay

object WelcomePageAnimations {

    // Button Slide-Up and Fade-In Animation
    fun slideUpAndFadeIn(view: View, duration: Long = 1000, delay: Long = 1000) {
        view.translationY = 200f
        view.alpha = 0f
        view.animate()
            .setStartDelay(delay)
            .translationY(0f)
            .alpha(1f)
            .setDuration(duration)
            .setInterpolator(DecelerateInterpolator())
            .start()
    }

    // Text Typewriter Effect
    fun typewriterEffect(textView: TextView, text: String, delayPerChar: Long = 20) {
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