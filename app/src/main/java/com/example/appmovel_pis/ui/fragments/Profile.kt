package com.example.appmovel_pis.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.objects.ScrollViewFuntion
import com.example.appmovel_pis.ui.menu.MenuPage

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Get the LinearLayout from the fragment's root view (after the view is inflated)
        val settingsButton: View = view.findViewById(R.id.settingsBTN)
        val scrollView = requireActivity().findViewById<View>(R.id.scrollView)
        val sizeChecker = requireActivity().findViewById<ImageView>(R.id.sizeChecker)
        val fragmentManager = requireActivity().supportFragmentManager

        // Now use the settingsButton from the fragment's layout
        ScrollViewFuntion.setupImageViewClickListener(
            view = settingsButton,  // Use the LinearLayout from the fragment
            fragment = SettingsFragment(),
            scrollView = scrollView,
            sizeChecker = sizeChecker,
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager
        )
    }
}