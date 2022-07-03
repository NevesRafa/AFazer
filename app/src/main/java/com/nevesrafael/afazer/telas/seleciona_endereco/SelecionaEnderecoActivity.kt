package com.nevesrafael.afazer.telas.seleciona_endereco

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nevesrafael.afazer.R
import com.nevesrafael.afazer.databinding.ActivitySelecionaEnderecoBinding
import com.nevesrafael.afazer.telas.cria_evento.CriaEventoFragment


class SelecionaEnderecoActivity : AppCompatActivity() {

    private lateinit var presenter: SelecionaEnderecoPresenter
    private lateinit var mapa: GoogleMap
    private lateinit var binding: ActivitySelecionaEnderecoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelecionaEnderecoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = SelecionaEnderecoPresenter(this)
        clickDoIcone()
        configuraFabConfirmar()

        //cria a variavel mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            mapa = map

            mostraNoMapaSeReceber()
        }
    }

    //click do icone
    private fun clickDoIcone() {
        binding.enderecoTextInput.setEndIconOnClickListener {
            val enderecoDigitado = binding.endereco.text.toString()

            presenter.procurarEndereco(enderecoDigitado)
        }
    }

    private fun mostraNoMapaSeReceber() {
        val longitude = intent.getDoubleExtra(CriaEventoFragment.EXTRA_EVENTO_LONGITUDE, 0.0)
        val latitude = intent.getDoubleExtra(CriaEventoFragment.EXTRA_EVENTO_LATITUDE, 0.0)
        val endereco = intent.getStringExtra(CriaEventoFragment.EXTRA_EVENTO_ENDERECO)

        presenter.carregaMapa(endereco, latitude, longitude)
    }

    fun marcaNoMapa(endereco: String, longitude: Double, latitude: Double) {

        binding.endereco.setText(endereco, TextView.BufferType.EDITABLE)

        val latLong = LatLng(latitude, longitude)
        val marcador = MarkerOptions().position(latLong).title(endereco)

        configuracaoParaMarcarLocalNaTela(latLong, marcador)
    }

    //marcar a localização no google maps
    fun marcaNoMapa(localizacao: Address) {
        val latLong = LatLng(localizacao.latitude, localizacao.longitude)
        val marcador = MarkerOptions().position(latLong).title(localizacao.getAddressLine(0))

        configuracaoParaMarcarLocalNaTela(latLong, marcador)
    }

    private fun configuraFabConfirmar() {
        binding.fabConfirmar.setOnClickListener {
            presenter.verificaEnderecoParaCarregar()
        }
    }

    fun mostraErroSemEndereco() {
        Toast.makeText(this, "Porfavor selecione um endereço", Toast.LENGTH_SHORT).show()
    }

    fun mostraErroEnderecoNaoEncontrado() {
        Toast.makeText(this@SelecionaEnderecoActivity, "Não encontrei o endereço", Toast.LENGTH_SHORT).show()
    }

    fun configuracaoParaMarcarLocalNaTela(latLong: LatLng, marcador: MarkerOptions) {
        mapa.clear()
        mapa.addMarker(marcador)

        val movimentoCamera = CameraUpdateFactory.newLatLngZoom(latLong, 15.0F)
        mapa.moveCamera(movimentoCamera)
    }

    fun mandaParaBottomSheetFragment(enderecoEncontrado: Address) {
        val intentParaRetornar = Intent()
        intentParaRetornar.putExtra(CriaEventoFragment.EXTRA_EVENTO_LATITUDE, enderecoEncontrado.latitude)
        intentParaRetornar.putExtra(CriaEventoFragment.EXTRA_EVENTO_LONGITUDE, enderecoEncontrado.longitude)
        intentParaRetornar.putExtra(CriaEventoFragment.EXTRA_EVENTO_ENDERECO, enderecoEncontrado.getAddressLine(0))

        // falo que o resultado foi OK e mando o pacotinho
        setResult(Activity.RESULT_OK, intentParaRetornar)

        // finalizo a tela
        finish()
    }

    fun mostraLoading() {
        binding.carregando.visibility = View.VISIBLE
    }

    fun escondeLoading() {
        binding.carregando.visibility = View.GONE
    }

}