package com.example.appmovel_pis.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.objects.ScrollViewFuntion

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

        // Get the LinearLayout from the fragment's root view (after the view is inflated)
        val goBackButton: View = view.findViewById(R.id.goBackBTN)
        val scrollView = requireActivity().findViewById<View>(R.id.scrollView)
        val sizeChecker = requireActivity().findViewById<ImageView>(R.id.sizeChecker)
        val fragmentManager = requireActivity().supportFragmentManager

        // Now use the settingsButton from the fragment's layout
        ScrollViewFuntion.setupImageViewClickListener(
            view = goBackButton,  // Use the LinearLayout from the fragment
            fragment = ProfileFragment(),
            scrollView = scrollView,
            sizeChecker = sizeChecker,
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager
        )
    }
}