package com.example.appmovel_pis.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.objects.ChangeFragment

class NotificationsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    // executa este código apenas quando acaba de criar a View
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Pega os componentes desejados pelo seus IDS
        val goBackButton: View = view.findViewById(R.id.goBackBTN)
        val fragmentManager = requireActivity().supportFragmentManager

        // Define a função do ScrollView para a aba de voltar para o Perfil
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