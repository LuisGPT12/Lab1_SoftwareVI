package com.example.lab1_softwarevi

import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

class Fragment_1 : Fragment() {
    private lateinit var dadoGif: ImageView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_1, container, false)
        dadoGif = view.findViewById(R.id.gifImageView)
        mostrarGif()
        return view
    }

    private fun mostrarGif() {
        val randomNum = (1..6).random()
        val gifId = resources.getIdentifier("dado$randomNum", "drawable", requireContext().packageName)

        Glide.with(this)
            .asGif()
            .load(gifId)
            .into(object : CustomTarget<GifDrawable>() {
                override fun onResourceReady(resource: GifDrawable, transition: Transition<in GifDrawable>?) {
                    resource.setLoopCount(1) // Solo una reproducción
                    dadoGif.setImageDrawable(resource)
                    resource.start()
                    Toast.makeText(requireContext(), "Resultado: $randomNum", Toast.LENGTH_SHORT).show()
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }
}
