package com.nevesrafael.afazer.telas.informacoesDoEvento

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nevesrafael.afazer.R
import com.nevesrafael.afazer.database.AppDatabase
import com.nevesrafael.afazer.database.EventoDao
import com.nevesrafael.afazer.databinding.ActivityInformacoesDoEventoBinding
import com.nevesrafael.afazer.model.Evento
import java.time.format.DateTimeFormatter
import java.util.*

class InformacoesDoEventoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInformacoesDoEventoBinding
    private lateinit var eventoDao: EventoDao
    private var eventoId: Int = 0
    private var evento: Evento? = null
    private lateinit var mapa: GoogleMap

    companion object {
        const val EXTRA_EVENTO_RECEBIDO_ID = "extra.evento.recebido.id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformacoesDoEventoBinding.inflate(layoutInflater)
        eventoDao = AppDatabase.instancia(this).eventoDao()
        setContentView(binding.root)
        eventoId = intent.getIntExtra(EXTRA_EVENTO_RECEBIDO_ID, 0)


        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            mapa = map
            carregaEvento()
        }
    }


    private fun carregaEvento() {
        evento = eventoDao.buscaPorId(eventoId)
        evento?.let { evento ->
            binding.informacaoEvento.setText(evento.evento, TextView.BufferType.EDITABLE)
            binding.informacaoDescricao.setText(evento.descricao, TextView.BufferType.EDITABLE)
            binding.informacaoEndereco.setText(evento.endereco, TextView.BufferType.EDITABLE)
            binding.informacaoData.setText(evento.data,TextView.BufferType.EDITABLE)



            if (evento.latitude != null && evento.longitude != null && evento.endereco != null) {
                marcaLocalizacaoNaTela(evento.latitude!!, evento.longitude!!, evento.endereco!!)
            }
        }

    }

    private fun marcaLocalizacaoNaTela(latitude: Double, longitude: Double, endereco: String) {
        val latLong = LatLng(latitude, longitude)
        val marcador = MarkerOptions().position(latLong).title(endereco)

        mapa.addMarker(marcador)

        val movimentoCamera = CameraUpdateFactory.newLatLngZoom(latLong, 15.0F)
        mapa.moveCamera(movimentoCamera)
    }

}