package com.example.appmovel_pis.ui.fragments

import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.appmovel_pis.R

class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        // Reference the ImageView for Sensores
        val sensoresImageView = view.findViewById<ImageView>(R.id.SensoresBTN)

        // Set the click listener
        sensoresImageView.setOnClickListener {
            // Replace with SensoresFragment
            val transaction = parentFragmentManager.beginTransaction()
            transaction.replace(R.id.menuFragmentContainer, SensorFragment())
            transaction.addToBackStack(null) // Allow back navigation
            transaction.commit()
        }

        return view
    }
}