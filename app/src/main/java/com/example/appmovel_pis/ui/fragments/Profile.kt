package com.example.appmovel_pis.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.objects.ScrollViewFuntion
import com.example.appmovel_pis.ui.objects.ClickAnimation

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    // executa este código apenas quando acaba de criar a View
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pega os componentes desejados pelo seus IDS
        val LayoutChangePassword = view.findViewById<LinearLayout>(R.id.LayoutPassword)
        val LayoutNotifications = view.findViewById<LinearLayout>(R.id.LayoutNotifications)
        val LayoutSettings = view.findViewById<LinearLayout>(R.id.LayoutSettings)
        val scrollView = requireActivity().findViewById<View>(R.id.scrollView)
        val sizeChecker = requireActivity().findViewById<ImageView>(R.id.sizeChecker)
        val fragmentManager = requireActivity().supportFragmentManager

        // Define a função do ScrollView para a aba de Definições
        ScrollViewFuntion.setupImageViewClickListener(
            view = LayoutSettings,
            fragment = SettingsFragment(),
            scrollView = scrollView,
            sizeChecker = sizeChecker,
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager
        )

        ScrollViewFuntion.setupImageViewClickListener(
            view = LayoutNotifications,
            fragment = NotificationsFragment(),
            scrollView = scrollView,
            sizeChecker = sizeChecker,
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager
        )

        ScrollViewFuntion.setupImageViewClickListener(
            view = LayoutChangePassword,
            fragment = ChangePasswordFragment(),
            scrollView = scrollView,
            sizeChecker = sizeChecker,
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager
        )

        ClickAnimation.applyTouchAnimation(LayoutChangePassword, requireContext())
        ClickAnimation.applyTouchAnimation(LayoutNotifications, requireContext())
        ClickAnimation.applyTouchAnimation(LayoutSettings, requireContext())

    }
}