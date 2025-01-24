package com.example.appmovel_pis.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.objects.ClickAnimation
import com.example.appmovel_pis.ui.objects.ChangeFragment

class SettingsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    // executa este código apenas quando acaba de criar a View
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pega os componentes desejados pelo seus IDS
        val LayoutUno = view.findViewById<LinearLayout>(R.id.LayoutUno)
        val LayoutDos = view.findViewById<LinearLayout>(R.id.LayoutDos)
        val LayoutTres = view.findViewById<LinearLayout>(R.id.LayoutTres)
        val goBackButton: View = view.findViewById(R.id.goBackBTN)
        val scrollView = requireActivity().findViewById<View>(R.id.scrollView)
        val fragmentManager = requireActivity().supportFragmentManager

        // Define a função do ScrollView para a aba de voltar para o Perfil
        ChangeFragment.setupImageViewClickListener(
            view = goBackButton,
            fragment = ProfileFragment(),
            scrollView = scrollView,
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager
        )

        ClickAnimation.applyTouchAnimation(LayoutUno, requireContext())
        ClickAnimation.applyTouchAnimation(LayoutDos, requireContext())
        ClickAnimation.applyTouchAnimation(LayoutTres, requireContext())
    }
}