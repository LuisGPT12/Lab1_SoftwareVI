package com.example.lab1_softwarevi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        auth = FirebaseAuth.getInstance()

        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val etPassword = view.findViewById<EditText>(R.id.etPassword)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val tvError = view.findViewById<TextView>(R.id.tvError)

        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            tvError.visibility = View.GONE

            if (email.isBlank() || password.isBlank()) {
                tvError.text = "Completa ambos campos"
                tvError.visibility = View.VISIBLE
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Navega a la pantalla principal despues de un login exitoso
                            (requireActivity() as MainActivity).navigateToMainScreen()
                        } else {
                            tvError.text = "Error: ${task.exception?.localizedMessage ?: "No se pudo iniciar sesión"}"
                            tvError.visibility = View.VISIBLE
                        }
                    }
            }
        }

        return view
    }
}