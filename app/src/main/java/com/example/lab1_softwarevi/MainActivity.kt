package com.example.lab1_softwarevi
import android.os.Bundle
import android.widget.Button
import android.animation.ValueAnimator
import android.animation.AnimatorSet
import android.graphics.drawable.ColorDrawable
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

/**
 * Justin Barrios, Cédula: 8-983-1021
 * Luis Monterrosa, Cédula: 8-1014-1095
 * Aaron Santamaria, Cédula: 3-742-1763
 **/

class MainActivity : AppCompatActivity(), DadoAnimListener {
    private lateinit var button: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        button = findViewById(R.id.button)

        button.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, Fragment_1())
                commit()
            }
        }
    }

    override fun onActivar() {
        button.isEnabled = false
    }

    override fun onDesactivar() {
        button.isEnabled = true
    }

    fun parpadearFondo(color: Int) {
        val background = findViewById<ImageView>(R.id.backgroundImage)
        val colorDrawable = ColorDrawable(color)
        colorDrawable.alpha = 0
        background.foreground = colorDrawable

        // Animación de aparición (0 -> 150)
        val fadeIn = ValueAnimator.ofInt(0, 150)
        fadeIn.duration = 500
        fadeIn.addUpdateListener {
            val alpha = it.animatedValue as Int
            colorDrawable.alpha = alpha
        }

        // Pausa simulada (150 -> 150, sin cambio pero con duración)
        val hold = ValueAnimator.ofInt(150, 150)
        hold.duration = 1000 // mantiene el color visible por 1 segundo

        // Animación de desaparición (150 -> 0)
        val fadeOut = ValueAnimator.ofInt(150, 0)
        fadeOut.duration = 500
        fadeOut.addUpdateListener {
            val alpha = it.animatedValue as Int
            colorDrawable.alpha = alpha
        }

        // Ejecutar en orden: aparecer → mantener → desaparecer
        val animatorSet = AnimatorSet()
        animatorSet.playSequentially(fadeIn, hold, fadeOut)
        animatorSet.start()
    }

}


