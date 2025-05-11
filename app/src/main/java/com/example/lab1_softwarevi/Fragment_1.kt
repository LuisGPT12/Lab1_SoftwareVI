package com.example.lab1_softwarevi

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import nl.dionsegijn.konfetti.xml.KonfettiView
import java.util.concurrent.TimeUnit

interface DadoAnimListener {
    fun onDesactivar()
    fun onActivar()
    fun parpadearFondo(color: Int)
    fun reproducirSonido(resultado: Int)
}

class Fragment_1 : Fragment() {

    private lateinit var dadoGif: ImageView
    private lateinit var imageResul: ImageView
    private lateinit var konfettiView: KonfettiView
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
        imageResul = view.findViewById(R.id.imageResul)
        konfettiView = view.findViewById(R.id.konfettiView)

        mostrarGif()
        return view
    }

    private fun mostrarGif() {
        listener?.onActivar()

        val randomNum = (1..6).random()
        val gifId = resources.getIdentifier("dado$randomNum", "drawable", requireContext().packageName)
        val imgId = resources.getIdentifier("num$randomNum", "drawable", requireContext().packageName)

        Glide.with(this)
            .asGif()
            .load(gifId)
            .into(object : CustomTarget<GifDrawable>() {
                override fun onResourceReady(resource: GifDrawable, transition: Transition<in GifDrawable>?) {
                    resource.setLoopCount(1)
                    dadoGif.setImageDrawable(resource)
                    resource.start()

                    dadoGif.postDelayed({
                        // Mostrar imagen del resultado
                        imageResul.setImageResource(imgId)
                        imageResul.visibility = View.VISIBLE

                        // Efectos visuales y sonidos
                        val color = if (randomNum == 6) Color.parseColor("#FFD700") else Color.GRAY
                        listener?.parpadearFondo(color)
                        listener?.reproducirSonido(randomNum)

                        if (randomNum == 6) {
                            lanzarKonfetti()
                        }

                        listener?.onDesactivar()
                    }, 2000)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }

    private fun lanzarKonfetti() {
        konfettiView.visibility = View.VISIBLE

        val positions = listOf(0.1, 0.3, 0.5, 0.7, 0.9)

        positions.forEach { x ->
            konfettiView.start(
                Party(
                    emitter = Emitter(duration = 100, TimeUnit.MILLISECONDS).max(50),
                    speed = 5f,
                    maxSpeed = 7f,
                    damping = 0.9f,
                    spread = 360,
                    colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
                    position = Position.Relative(x, 0.0),
                )
            )
        }

        konfettiView.postDelayed({
            konfettiView.visibility = View.GONE
        }, 3000)
    }

}
