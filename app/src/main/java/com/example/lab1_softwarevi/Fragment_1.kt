package com.example.lab1_softwarevi
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

interface DadoAnimListener {
    fun onDesactivar()
    fun onActivar()
}

class Fragment_1 : Fragment() {
    private lateinit var dadoGif: ImageView
    private lateinit var textResul: TextView
    private var listener: DadoAnimListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DadoAnimListener) {
            listener = context
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_1, container, false)
        dadoGif = view.findViewById(R.id.gifImageView)
        textResul = view.findViewById(R.id.daResult)
        mostrarGif()
        return view
    }

    private fun mostrarGif() {
        listener?.onActivar()
        val randomNum = (1..6).random()
        val gifId = resources.getIdentifier("dado$randomNum", "drawable", requireContext().packageName)

        Glide.with(this)
            .asGif()
            .load(gifId)
            .into(object : CustomTarget<GifDrawable>() {
                override fun onResourceReady(resource: GifDrawable, transition: Transition<in GifDrawable>?) {
                    resource.setLoopCount(1)
                    dadoGif.setImageDrawable(resource)
                    resource.start()
                    dadoGif.postDelayed({
                        textResul.text = randomNum.toString()
                        listener?.onDesactivar()
                    }, 2000)
                }

                override fun onLoadCleared(placeholder: Drawable?) {

                }
            })
    }
}




