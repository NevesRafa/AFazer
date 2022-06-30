package com.nevesrafael.afazer.telas.tela_incial

import android.view.MenuItem
import com.nevesrafael.afazer.R
import com.nevesrafael.afazer.database.AppDatabase
import com.nevesrafael.afazer.model.Evento

class TelaInicialPresenter(val telaInicial: TelaInicialActivity) {

    private val eventoDao = AppDatabase.instancia(telaInicial).eventoDao()

    fun quandoClicaNoSalvar(eventoParaSalvar: Evento) {
        // feito o clique no salvar, quem tem que adicionar no dao é o presenter (lógica)

        // agora vamos por partes
        // primeiro eu vou salvar esse evento lá no banco
        eventoDao.salvar(eventoParaSalvar)

        atualizaListaNaTela()
    }


    /**
     * evento é o evento selecionado no adapter
     * taSelecionado é se foi checkado ou não
     */
    fun quandoClicarNoRadio(evento: Evento, taSelecionado: Boolean) {
        // primeiro a gente atualiza o objeto evento pra falar se foi selecionado ou não
        evento.finalizado = taSelecionado

        // alteramos no DAO
        eventoDao.altera(evento)

        // agora aqui o negócio fica legal..
        // ali no metodo de cima a gente já fez isso, buscar uma lista atualizada
        // e mostrar na tela
        atualizaListaNaTela()
    }

    fun quandoClicarNoMenu(menuItem: MenuItem, evento: Evento) {

        if (menuItem.itemId == R.id.menu_excluir) {
            eventoDao.remove(evento)
            atualizaListaNaTela()

        } else if (menuItem.itemId == R.id.menu_editar) {
            telaInicial.mostraModalDeEditar(evento)
        }
    }

    fun quandoClicaNoEditar(eventoParaEditar: Evento) {
        eventoDao.altera(eventoParaEditar)

        atualizaListaNaTela()
    }

    fun atualizaListaNaTela() {
        val listaDeEventos = eventoDao.buscaTodos()
        telaInicial.mostraListaDeEventos(listaDeEventos)
    }
}
