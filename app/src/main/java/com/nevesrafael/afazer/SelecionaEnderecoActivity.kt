package com.nevesrafael.afazer

import android.app.Activity
import android.content.Intent
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.nevesrafael.afazer.databinding.ActivitySelecionaEnderecoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException


class SelecionaEnderecoActivity : AppCompatActivity() {

    private lateinit var mapa: GoogleMap
    private lateinit var binding: ActivitySelecionaEnderecoBinding
    private var enderecoSelecionado: Address? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelecionaEnderecoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickDoIcone()
        configuraFabConfirmar()

        //cria a variavel mapa
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync { map ->
            mapa = map
        }
    }

    private fun configuraFabConfirmar() {
        binding.fabConfirmar.setOnClickListener {
            if (enderecoSelecionado != null){
                mandaParaBottomSheetFragment()
            }else{
                Toast.makeText(this, "Porfavor selecione um endereço", Toast.LENGTH_SHORT).show()
            }
        }
    }

    //click do icone
    private fun clickDoIcone() {
        binding.enderecoTextInput.setEndIconOnClickListener {
            val enderecoDigitado = binding.endereco.text.toString()

            //Abre linha do tempo paralela na MAIN
            lifecycleScope.launch {

                binding.carregando.visibility = View.VISIBLE

                //Passa a responsabilidade da MAIN para HD (IO)
                val addressEncontrado = withContext(Dispatchers.IO) {
                    return@withContext buscaAddressDoEndereco(enderecoDigitado)
                }

                binding.carregando.visibility = View.GONE

                //vou utilizar o enderecoSelecionado no botao confirmar
                enderecoSelecionado = addressEncontrado

                if (addressEncontrado != null) {
                    marcaLocalizacaoNaTela(addressEncontrado)
                } else {
                    Toast.makeText(
                        this@SelecionaEnderecoActivity,
                        "Não encontrei o endereço",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    //marcar a localização no google maps
    private fun marcaLocalizacaoNaTela(localizacao: Address) {
        val latLong = LatLng(localizacao.latitude, localizacao.longitude)
        val marcador = MarkerOptions().position(latLong).title(localizacao.getAddressLine(0))

        mapa.addMarker(marcador)

        val movimentoCamera = CameraUpdateFactory.newLatLngZoom(latLong, 15.0F)
        mapa.moveCamera(movimentoCamera)
    }

    //busca a latitude e longitudo pelo endereço
    fun buscaAddressDoEndereco(endereco: String?): Address? {
        val coder = Geocoder(this)

        try {
            val tentativa = coder.getFromLocationName(endereco, 1)
            if (tentativa == null || tentativa.size == 0) {
                return null
            }
            return tentativa[0]
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

    }

    fun mandaParaBottomSheetFragment(){
        val intentParaRetornar = Intent()
        intentParaRetornar.putExtra(FragmentModalSalvar.EXTRA_EVENTO_LATITUDE,enderecoSelecionado?.latitude)
        intentParaRetornar.putExtra(FragmentModalSalvar.EXTRA_EVENTO_LONGITUDE,enderecoSelecionado?.longitude)
        intentParaRetornar.putExtra(FragmentModalSalvar.EXTRA_EVENTO_ENDERECO,enderecoSelecionado?.getAddressLine(0))

        // falo que o resultado foi OK e mando o pacotinho
        setResult(Activity.RESULT_OK, intentParaRetornar)

        // finalizo a tela
        finish()
    }


}