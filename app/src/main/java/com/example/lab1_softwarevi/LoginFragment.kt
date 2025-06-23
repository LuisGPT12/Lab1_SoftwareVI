package com.example.lab1_softwarevi

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment

class LoginFragment : Fragment() {

    private lateinit var firebaseManager: FirebaseManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)
        firebaseManager = FirebaseManager()

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
                firebaseManager.login(email, password) { success, error ->
                    if (success) {
                        (requireActivity() as MainActivity).showMainScreen()
                    } else {
                        tvError.text = when {
                            error?.contains("password is invalid", ignoreCase = true) == true ||
                                    error?.contains("no user record", ignoreCase = true) == true ||
                                    error?.contains("auth credential is incorrect", ignoreCase = true) == true -> {
                                "Correo o contraseña incorrectos."
                            }
                            error?.contains("network error", ignoreCase = true) == true -> {
                                "Error de red. Intenta de nuevo."
                            }
                            else -> "Error: ${error ?: "No se pudo iniciar sesión"}"
                        }
                        tvError.visibility = View.VISIBLE
                    }
                }
            }
        }

        return view
    }
}