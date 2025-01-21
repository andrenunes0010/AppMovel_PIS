package com.example.appmovel_pis.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.objects.ScrollViewFuntion
import com.example.appmovel_pis.ui.objects.ClickAnimation

class WelcomePageFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome_page, container, false)
    }

    // executa este código apenas quando acaba de criar a View
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pega os componentes desejados pelo seus IDS
        val ButtonStart = view.findViewById<Button>(R.id.GetStartedBtn)
        val scrollView = requireActivity().findViewById<View>(R.id.scrollView)
        val fragmentManager = requireActivity().supportFragmentManager

        // Define a função do ScrollView para a aba de Definições
        ScrollViewFuntion.setupImageViewClickListener(
            view = ButtonStart,
            fragment = LoginFragment(),
            scrollView = scrollView,
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager
        )

        ClickAnimation.applyTouchAnimation(ButtonStart, requireContext())

    }
}