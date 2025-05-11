package com.example.lab1_softwarevi

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

/**
 * Justin Barrios, Cédula: 8-983-1021
 * Luis Monterrosa, Cédula: 8-1014-1095
 * Aaron Santamaría, Cédula: 3-742-1763
 **/

class MainActivity : AppCompatActivity(), DadoAnimListener {

    private lateinit var button: Button
    private lateinit var backgroundImage: ImageView

    private lateinit var sonidoBoton: MediaPlayer
    private lateinit var sonidoGiro: MediaPlayer
    private lateinit var sonidoVictoria: MediaPlayer
    private lateinit var sonidoDerrota: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        button = findViewById(R.id.button)
        backgroundImage = findViewById(R.id.backgroundImage)

        sonidoBoton = MediaPlayer.create(this, R.raw.boton)
        sonidoGiro = MediaPlayer.create(this, R.raw.giro)
        sonidoVictoria = MediaPlayer.create(this, R.raw.victory)
        sonidoDerrota = MediaPlayer.create(this, R.raw.derrota)

        button.setOnClickListener {
            sonidoBoton.start()
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragment_container, Fragment_1())
                commit()
            }
        }
    }

    override fun onActivar() {
        button.isEnabled = false
        sonidoGiro.start()
    }

    override fun onDesactivar() {
        button.isEnabled = true
    }

    override fun reproducirSonido(resultado: Int) {
        if (resultado == 6) {
            sonidoVictoria.start()
        } else {
            sonidoDerrota.start()
        }
    }

    override fun parpadearFondo(color: Int) {
        val background = findViewById<ImageView>(R.id.backgroundImage)
        val colorDrawable = ColorDrawable(color)
        colorDrawable.alpha = 0
        background.foreground = colorDrawable

        // Animación de aparición (0 → 150)
        val fadeIn = ValueAnimator.ofInt(0, 150)
        fadeIn.duration = 500
        fadeIn.addUpdateListener {
            colorDrawable.alpha = it.animatedValue as Int
        }

        // Pausa (150 → 150, mantiene visible 1 segundo)
        val hold = ValueAnimator.ofInt(150, 150)
        hold.duration = 1000

        // Animación de desaparición (150 → 0)
        val fadeOut = ValueAnimator.ofInt(150, 0)
        fadeOut.duration = 500
        fadeOut.addUpdateListener {
            colorDrawable.alpha = it.animatedValue as Int
        }

        // Ejecutar en orden
        val animatorSet = android.animation.AnimatorSet()
        animatorSet.playSequentially(fadeIn, hold, fadeOut)
        animatorSet.start()
    }

}
