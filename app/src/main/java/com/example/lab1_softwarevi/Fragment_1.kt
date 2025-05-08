package com.example.lab1_softwarevi

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.VideoView
import androidx.fragment.app.Fragment

class Fragment_1 : Fragment() {

    private lateinit var videodado: VideoView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_1, container, false)
        videodado = view.findViewById(R.id.dadoVideo)
        randomDice()
        return view
    }

    private fun randomDice() {
        val randomNumber = (1..6).random()
        val videoName = "dado$randomNumber"
        val videoResId = resources.getIdentifier(videoName, "raw", requireContext().packageName)
        val uri = Uri.parse("android.resource://${requireContext().packageName}/$videoResId")
        videodado.setVideoURI(uri)
        videodado.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = false
        }
        videodado.setOnCompletionListener {
            Toast.makeText(requireContext(), "Resultado: $randomNumber", Toast.LENGTH_SHORT).show()
        }
        videodado.start()
    }
}
