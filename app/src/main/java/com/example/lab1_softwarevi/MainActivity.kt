package com.example.lab1_softwarevi
import android.media.MediaPlayer
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar


/**
 * Justin Barrios, Cédula: 8-983-1021
 * Luis Monterrosa, Cédula: 8-1014-1095
 * Aaron Santamaria, Cédula: 3-742-1763
 **/

class MainActivity : AppCompatActivity() {
    private var fragment1: Fragment_1? = null
    private lateinit var button: Button

  override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.main_activity)

      button = findViewById(R.id.button)
      if (savedInstanceState == null) {
          fragment1 = Fragment_1()
          supportFragmentManager.beginTransaction()
              .add(R.id.fragment_container, fragment1!!)
              .commit()
      } else {
          fragment1 =
              supportFragmentManager.findFragmentById(R.id.fragment_container) as Fragment_1?
      }
      button.setOnClickListener {
          fragment1?.hiddenNum()
          button.isEnabled = false
          val boton = MediaPlayer.create(this, R.raw.boton)
          boton.setOnCompletionListener {
              val tiro = MediaPlayer.create(this, R.raw.giro)
              fragment1?.mostrarGif()
              tiro.setOnCompletionListener {
                  val num = fragment1?.audioNum?:0
                  if (num != null && num in 1..6) {
                      val resulAu= if (num==6) R.raw.victory else R.raw.derrota
                      val finAudio = MediaPlayer.create(this, resulAu)
                      finAudio.setOnCompletionListener {
                          button.isEnabled = true
              }
                  finAudio.start() } else{ button.isEnabled = true}
          }
              tiro.start()
          }
          boton.start()

      }

  }
}

