package com.example.appmovel_pis.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.appmovel_pis.R
import com.example.appmovel_pis.ui.objects.ChangeFragment
import com.example.appmovel_pis.repository.BaseDadosManager

import kotlinx.coroutines.launch


class ChangePasswordFragment : Fragment() {

    private lateinit var btnSubmeter: Button
    private lateinit var etCurrentPassword: EditText
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_change_password, container, false)
    }

    // executa este código apenas quando acaba de criar a View
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSubmeter = view.findViewById(R.id.btnSubmeter)
        etCurrentPassword = view.findViewById(R.id.et_current_password)
        etNewPassword = view.findViewById(R.id.et_new_password)
        etConfirmPassword = view.findViewById(R.id.et_confirm_password)

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

        val baseDadosManager = BaseDadosManager(requireContext())

        btnSubmeter.setOnClickListener {
            val currentPassword = etCurrentPassword.text.toString()
            val newPassword = etNewPassword.text.toString()
            val confirmPassword = etConfirmPassword.text.toString()

            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(requireContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            } else if (!isValidPassword(newPassword)) {
                Toast.makeText(requireContext(), "A senha deve ter pelo menos 6 caracteres e um caractere especial!", Toast.LENGTH_LONG).show()
            } else if (newPassword == confirmPassword) {
                // Lançar a coroutine
                viewLifecycleOwner.lifecycleScope.launch {
                    val success = baseDadosManager.alterarPassword(
                        currentPassword = currentPassword,
                        newPassword = newPassword
                    )

                }
            } else {
                Toast.makeText(requireContext(), "As passwords não coincidem!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun isValidPassword(password: String): Boolean {
        val specialCharPattern = Regex(".*[!@#\$%^&*(),.?\":{}|<>].*")
        return password.length >= 6 && specialCharPattern.containsMatchIn(password)
    }
}
