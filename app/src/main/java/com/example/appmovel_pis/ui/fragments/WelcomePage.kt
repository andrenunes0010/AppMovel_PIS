package com.example.appmovel_pis.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.airbnb.lottie.LottieAnimationView
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.objects.WelcomePageAnimations
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

        // Initialize Lottie Animation
        val fallingLeavesAnimation = view.findViewById<LottieAnimationView>(R.id.fallingLeavesAnimation)
        fallingLeavesAnimation.setAnimation(R.raw.falling_leaves) // Optional if defined in XML
        fallingLeavesAnimation.playAnimation()

        // Pega os componentes desejados pelo seus IDS
        val appLogo = view.findViewById<ImageView>(R.id.appLogo)
        val descriptionText = view.findViewById<TextView>(R.id.DescriptionText)
        val getStartedBtn = view.findViewById<Button>(R.id.GetStartedBtn)
        val scrollView = requireActivity().findViewById<View>(R.id.scrollView)
        val fragmentManager = requireActivity().supportFragmentManager

        // Define a função do ScrollView para a aba de Definições
        ScrollViewFuntion.setupImageViewClickListener(
            view = getStartedBtn,
            fragment = LoginFragment(),
            scrollView = scrollView,
            fragmentContainerId = R.id.menuFragmentContainer,
            fragmentManager = fragmentManager
        )

        ClickAnimation.applyTouchAnimation(getStartedBtn, requireContext())

        // Apply animations
        WelcomePageAnimations.zoomIn(appLogo) // Logo animation
        WelcomePageAnimations.typewriterEffect(descriptionText, "Comece a salvar florestas com apenas um clique de um botão.") // Typewriter effect
        WelcomePageAnimations.slideUpAndFadeIn(getStartedBtn) // Slide up button animation


    }
}