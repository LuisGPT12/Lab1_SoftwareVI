package com.example.lab1_softwarevi

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseManager {
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()

    // Login
    fun login(email: String, password: String, callback: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    callback(true, null)
                } else {
                    callback(false, task.exception?.localizedMessage)
                }
            }
    }

    // Cerrar sesión
    fun logout(callback: () -> Unit) {
        auth.signOut()
        callback()
    }

    // Obtener los puntos actuales del usuario
    fun getUserPoints(uid: String, callback: (String?, Long?) -> Unit) {
        db.collection("registro_de_puntos")
            .whereEqualTo("usuario_id", uid)
            .limit(1)
            .get()
            .addOnSuccessListener { documents ->
                if (!documents.isEmpty) {
                    val doc = documents.documents[0]
                    val puntos = doc.getLong("cantidad_puntos")
                    callback(doc.id, puntos)
                } else {
                    callback(null, null)
                }
            }
            .addOnFailureListener {
                callback(null, null)
            }
    }

    // Actualizar los puntos del usuario
    fun updateUserPoints(docId: String, nuevosPuntos: Long, callback: (Boolean) -> Unit) {
        db.collection("registro_de_puntos").document(docId)
            .update("cantidad_puntos", nuevosPuntos)
            .addOnSuccessListener { callback(true) }
            .addOnFailureListener { callback(false) }
    }

}