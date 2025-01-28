package com.example.appmovel_pis.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.objects.ChangeFragment
import com.example.appmovel_pis.ui.objects.ClickAnimation
import com.example.appmovel_pis.data.SessionManager

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }


    // executa este código apenas quando acaba de criar a View
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pega os componentes desejados pelo seus IDS
        val LayoutChangePassword = view.findViewById<LinearLayout>(R.id.LayoutPassword)
        val LayoutNotifications = view.findViewById<LinearLayout>(R.id.LayoutNotifications)
        val LayoutSettings = view.findViewById<LinearLayout>(R.id.LayoutSettings)
        val scrollView = requireActivity().findViewById<View>(R.id.scrollView)
        val fragmentManager = requireActivity().supportFragmentManager

        // Define a função do ScrollView para a aba de Definições
        ChangeFragment.setupImageViewClickListener(
            view = LayoutSettings,
            fragment = SettingsFragment(),
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager,
            enterAnimation = R.anim.slide_in_right,
            exitAnimation = R.anim.slide_out_left,
            popEnterAnimation = R.anim.slide_in_left,
            popExitAnimation = R.anim.slide_out_right
        )

        ChangeFragment.setupImageViewClickListener(
            view = LayoutNotifications,
            fragment = NotificationsFragment(),
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager,
            enterAnimation = R.anim.slide_in_right,
            exitAnimation = R.anim.slide_out_left,
            popEnterAnimation = R.anim.slide_in_left,
            popExitAnimation = R.anim.slide_out_right
        )

        ChangeFragment.setupImageViewClickListener(
            view = LayoutChangePassword,
            fragment = ChangePasswordFragment(),
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager,
            enterAnimation = R.anim.slide_in_right,
            exitAnimation = R.anim.slide_out_left,
            popEnterAnimation = R.anim.slide_in_left,
            popExitAnimation = R.anim.slide_out_right
        )

        ClickAnimation.applyTouchAnimation(LayoutChangePassword, requireContext())
        ClickAnimation.applyTouchAnimation(LayoutNotifications, requireContext())
        ClickAnimation.applyTouchAnimation(LayoutSettings, requireContext())

    }
}