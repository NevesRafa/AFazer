package com.nevesrafael.afazer.telas.seleciona_endereco

import android.location.Address
import android.location.Geocoder
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

class SelecionaEnderecoPresenter(val tela: SelecionaEnderecoActivity) {

    private var enderecoEncontrado: Address? = null
    val coder = Geocoder(tela)

    fun carregaMapa(endereco: String?, latitude: Double, longitude: Double) {

        if (latitude != 0.0 && longitude != 0.0 && endereco != null) {
            tela.marcaNoMapa(endereco, longitude, latitude)
        }
    }

    fun verificaEnderecoParaCarregar() {
        if (enderecoEncontrado != null) {
            tela.mandaParaBottomSheetFragment(enderecoEncontrado!!)
        } else {
            tela.mostraErroSemEndereco()
        }
    }

    fun procurarEndereco(enderecoDigitado: String?) {

        //Abre linha do tempo paralela na MAIN
        tela.lifecycleScope.launch {

            tela.mostraLoading()

            //Passa a responsabilidade da MAIN para HD (IO)
            enderecoEncontrado = withContext(Dispatchers.IO) {
                return@withContext buscaAddressDoEndereco(enderecoDigitado)
            }

            tela.escondeLoading()
        }

        if (enderecoEncontrado != null) {
            tela.marcaNoMapa(enderecoEncontrado!!)
        } else {
            tela.mostraErroEnderecoNaoEncontrado()
        }
    }

    //busca a latitude e longitudo pelo endereço
    fun buscaAddressDoEndereco(enderecoDigitado: String?): Address? {
        try {
            val tentativa = coder.getFromLocationName(enderecoDigitado, 1)
            if (tentativa == null || tentativa.size == 0) {
                return null
            }
            return tentativa[0]
        } catch (ex: IOException) {
            ex.printStackTrace()
            return null
        }

    }
}
