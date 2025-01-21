package com.example.appmovel_pis.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.objects.ScrollViewFuntion

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
        val scrollView = requireActivity().findViewById<View>(R.id.scrollView)
        val fragmentManager = requireActivity().supportFragmentManager

        // Define a função do ScrollView para a aba de voltar para o Perfil
        ScrollViewFuntion.setupImageViewClickListener(
            view = goBackButton,
            fragment = ProfileFragment(),
            scrollView = scrollView,
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager
        )
    }
}