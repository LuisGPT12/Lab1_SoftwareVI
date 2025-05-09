package com.example.lab1_softwarevi
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
  override fun onCreate(savedInstanceState: Bundle?) {
      super.onCreate(savedInstanceState)
      setContentView(R.layout.main_activity)
      val toolbar = findViewById<Toolbar>(R.id.toolbar)
      setSupportActionBar(toolbar)
      val button = findViewById<Button>(R.id.button)
      button.setOnClickListener {
          supportFragmentManager.beginTransaction().apply {
              replace(R.id.fragment_container, Fragment_1())
              commit()
          }
      }
  }
}

