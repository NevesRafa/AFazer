package com.nevesrafael.afazer.telas.informacoes_evento

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdate
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

class InformacoesDoEventoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInformacoesDoEventoBinding
    private var eventoId: Int = 0
    private lateinit var mapa: GoogleMap
    private lateinit var presenter: InformacoesDoEventoPresenter

    companion object {
        const val EXTRA_EVENTO_RECEBIDO_ID = "extra.evento.recebido.id"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInformacoesDoEventoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        eventoId = intent.getIntExtra(EXTRA_EVENTO_RECEBIDO_ID, 0)
        presenter = InformacoesDoEventoPresenter(this)


        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapa) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            mapa = map
            presenter.carregaEvento(eventoId)
        }
    }

    fun preencheEvento(evento: Evento){
        binding.informacaoEvento.setText(evento.evento, TextView.BufferType.EDITABLE)
        binding.informacaoDescricao.setText(evento.descricao, TextView.BufferType.EDITABLE)
        binding.informacaoEndereco.setText(evento.endereco, TextView.BufferType.EDITABLE)
        binding.informacaoData.setText(evento.data, TextView.BufferType.EDITABLE)
        binding.informacaoHorario.setText(evento.horario, TextView.BufferType.EDITABLE)
    }

    fun marcaLocalizacaoNaTela(latitude: Double, longitude: Double, endereco: String) {
        val latLong = LatLng(latitude, longitude)
        val marcador = MarkerOptions().position(latLong).title(endereco)

        mapa.addMarker(marcador)

        val movimentoCamera = CameraUpdateFactory.newLatLngZoom(latLong, 15.0F)
        mapa.moveCamera(movimentoCamera)
    }

    fun fechaTela() {
        finish()
    }
}