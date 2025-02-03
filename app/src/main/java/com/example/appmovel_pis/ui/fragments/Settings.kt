package com.example.appmovel_pis.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.objects.ChangeFragment

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireActivity().getSharedPreferences("settings", android.content.Context.MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("dark_mode", false)

        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
        )

        val darkModeSwitch = view.findViewById<SwitchCompat>(R.id.ModeSwitch)
        darkModeSwitch.isChecked = isDarkMode

        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            AppCompatDelegate.setDefaultNightMode(
                if (isChecked) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO
            )

            // Save preference
            with(prefs.edit()) {
                putBoolean("dark_mode", isChecked)
                apply()
            }
        }

        val goBackButton: View = view.findViewById(R.id.goBackBTN)
        val fragmentManager = requireActivity().supportFragmentManager

        ChangeFragment.setupImageViewClickListener(
            view = goBackButton,
            fragment = ProfileFragment(),
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager,
            enterAnimation = R.anim.slide_in_left,
            exitAnimation = R.anim.slide_out_right,
            popEnterAnimation = R.anim.slide_in_right,
            popExitAnimation = R.anim.slide_out_left
        )
    }
}
