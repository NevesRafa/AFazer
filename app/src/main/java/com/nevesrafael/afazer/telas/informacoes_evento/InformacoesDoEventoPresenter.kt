package com.nevesrafael.afazer.telas.informacoes_evento

import com.nevesrafael.afazer.database.AppDatabase

class InformacoesDoEventoPresenter(val tela: InformacoesDoEventoActivity) {

    private val eventoDao = AppDatabase.instancia(tela).eventoDao()

    fun carregaEvento(eventoId: Int) {

        val evento = eventoDao.buscaPorId(eventoId)

        if (evento != null) {
            tela.preencheEvento(evento)

            if (evento.latitude != null && evento.longitude != null && evento.endereco != null) {
                tela.marcaLocalizacaoNaTela(
                    evento.latitude!!,
                    evento.longitude!!,
                    evento.endereco!!
                )
            }
        } else {
            tela.fechaTela()
        }
    }
}