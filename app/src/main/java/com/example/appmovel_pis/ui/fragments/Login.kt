import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

class LoginFragment : Fragment(R.layout.fragment_login) {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val emailEditText = view.findViewById<EditText>(R.id.etEmail)
        val passwordEditText = view.findViewById<EditText>(R.id.etPassword)
        val loginButton = view.findViewById<Button>(R.id.btnLogin)

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
            } else {
                sharedViewModel.setEmail(email)
                sharedViewModel.setPassword(password)
                realizarLogin(email, password)
            }
        }
    }

    private fun realizarLogin(email: String, senha: String) {
        // Use the login logic here or call a method in the ViewModel/Activity
        CoroutineScope(Dispatchers.Main).launch {
            val authManager = BaseDadosManager(requireContext())
            val utilizador = authManager.autenticar(email, senha)

            withContext(Dispatchers.Main) {
                if (utilizador != null) {
                    Toast.makeText(requireContext(), "Bem-vindo, ${utilizador.nome}!", Toast.LENGTH_SHORT).show()
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(R.id.menuFragmentContainer, MenuPageFragment())
                        .commit()
                } else {
                    Toast.makeText(requireContext(), "Login falhou!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
