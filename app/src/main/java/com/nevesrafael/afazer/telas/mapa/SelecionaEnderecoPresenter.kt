package com.nevesrafael.afazer.telas.mapa

class SelecionaEnderecoPresenter(val tela: SelecionaEnderecoActivity) {

    private var enderecoSelecionado: String? = null
    private var latitudeSelecionada: Double? = null
    private var longitudeSelecionada: Double? = null


    fun carregaMapa(endereco: String?, latitude: Double, longitude: Double) {

        enderecoSelecionado = endereco
        longitudeSelecionada = longitude
        latitudeSelecionada = latitude

        if (latitude != 0.0 && longitude != 0.0 && endereco != null) {
            tela.marcaNoMapa(endereco, longitude, latitude)
        }
    }

    fun verificaEnderecoSelecionado() {
        if (enderecoSelecionado != null) {
            tela.mandaParaBottomSheetFragment()
        } else {
            tela.mostraToast()
        }
    }
}
