package com.example.lab1_softwarevi

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
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
    fun onActivarBotton()
    fun onDesactivarBotton()
    fun parpadearFondo(color: Int)
    fun reproducirSonido(resultado: Int)
}

class Fragment_1 : Fragment() {

    private lateinit var dadoGif: ImageView
    private lateinit var imageResul: ImageView
    private lateinit var konfettiView: KonfettiView
    private var listener: DadoAnimListener? = null
    private lateinit var imageresoult: ImageView
    private lateinit var textpoint : TextView

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
        imageresoult= view.findViewById(R.id.imageView2)
        textpoint= view.findViewById(R.id.textView)
        actualizarframe()
        mostrarGif()
        return view
    }
//funcion para mostrar el gif
private fun mostrarGif() {
    verificarPuntos { puedeJugar ->
        if (!isAdded) return@verificarPuntos // El fragmento ya no está adjunto
        if (!puedeJugar) {
            Toast.makeText(activity, "No tienes puntos suficientes", Toast.LENGTH_SHORT).show()
            return@verificarPuntos
        }
        listener?.onDesactivarBotton()
        val randomNum = (1..6).random()
        val gifId = resources.getIdentifier("dado$randomNum", "drawable", requireContext().packageName)
        val imgId = resources.getIdentifier("num$randomNum", "drawable", requireContext().packageName)

        Glide.with(this)
            .asGif()
            .load(gifId)
            .into(object : CustomTarget<GifDrawable>() {
                override fun onResourceReady(
                    resource: GifDrawable,
                    transition: Transition<in GifDrawable>?
                ) {
                    if (!isAdded) return
                    resource.setLoopCount(1)
                    dadoGif.setImageDrawable(resource)
                    resource.start()

                    dadoGif.postDelayed({
                        if (!isAdded) return@postDelayed
                        imageResul.setImageResource(imgId)
                        imageResul.visibility = View.VISIBLE

                        val color = if (randomNum == 6) Color.parseColor("#FFD700") else Color.parseColor("#4C4F56")
                        listener?.parpadearFondo(color)
                        listener?.reproducirSonido(randomNum)
                        cambiarImgResoult(randomNum)
                        actualizarPuntos(randomNum)
                        if (randomNum == 6) {
                            lanzarKonfetti()
                        }
                    }, 1000)
                }

                override fun onLoadCleared(placeholder: Drawable?) {}
            })
    }
}


//funcion que lanza confeti
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
    private fun cambiarImgResoult(res: Int){
        imageresoult.visibility=View.VISIBLE
        if(res==6)imageresoult.setImageResource(R.drawable.victoria) else imageresoult.setImageResource(R.drawable.derrota)
    }
    private fun actualizarPuntos(resultado: Int) {
        val mainActivity = activity as? MainActivity ?: return
        val firebaseManager = mainActivity.firebaseManager
        val user = mainActivity.auth.currentUser ?: return

        firebaseManager.getUserPoints(user.uid) { docId, puntosActuales ->
            if (docId != null && puntosActuales != null) {

                val nuevosPuntos =
                    if (resultado == 6) puntosActuales + 500 else puntosActuales - 100
                firebaseManager.updateUserPoints(docId, nuevosPuntos) { exito ->
                    if (exito) {
                        actualizarframe()
                    }
                }
            }

        }}
    fun actualizarframe(){
        val mainActivity = activity as? MainActivity ?: return
        val firebaseManager = mainActivity.firebaseManager
        val user = mainActivity.auth.currentUser ?: return
        firebaseManager.getUserPoints(user.uid) { _, puntosnew ->
        if (puntosnew != null) {
            firebaseManager.getUserData(user.uid) { data ->
                val nombre = data?.get("nombre_completo") as? String ?: "???"
                textpoint.text = "Usuario: $nombre - Puntos: $puntosnew"
            }
            }
            }
    }
    fun verificarPuntos(callback: (Boolean) -> Unit) {
        val mainActivity = activity as? MainActivity ?: return
        val firebaseManager = mainActivity.firebaseManager
        val user = mainActivity.auth.currentUser ?: return
        firebaseManager.getUserPoints(user.uid) { _, puntosactuales ->
            if (puntosactuales == null  ||  puntosactuales <=0) {
                callback(false)
            }else{
                callback(true)
            }
    }
}
    fun limpiarPuntos() {
        if (this::textpoint.isInitialized) {
            textpoint.text = ""
        }
    }
}
