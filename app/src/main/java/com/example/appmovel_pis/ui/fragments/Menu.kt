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
        return inflater.inflate(R.layout.fragment_menu, container, false)
    }
}
//<androidx.fragment.app.FragmentContainerView
//        android:id="@+id/menuFragmentContainer"
//        android:layout_width="0dp"
//        android:layout_height="wrap_content"
//        app:layout_constraintTop_toBottomOf="@id/headerDivider"
//        app:layout_constraintBottom_toTopOf="@id/footerDivider"
//        app:layout_constraintStart_toStartOf="parent"
//        app:layout_constraintEnd_toEndOf="parent"
//        android:layout_marginTop="16dp"
//        android:layout_marginBottom="16dp" />