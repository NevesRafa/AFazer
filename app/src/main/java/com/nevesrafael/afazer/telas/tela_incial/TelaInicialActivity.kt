package com.nevesrafael.afazer.telas.tela_incial

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.nevesrafael.afazer.R
import com.nevesrafael.afazer.databinding.ActivityTelaInicialBinding
import com.nevesrafael.afazer.model.Evento
import com.nevesrafael.afazer.telas.cria_evento.FragmentModalSalvar
import com.nevesrafael.afazer.telas.informacoes_evento.InformacoesDoEventoActivity

class TelaInicialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTelaInicialBinding
    private lateinit var eventoAdapter: TelaInicialAdapter
    private lateinit var presenter: TelaInicialPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaInicialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = TelaInicialPresenter(this)

        configuraFabAddEvento()
        configuraRecyclerViewEvento()
    }

    override fun onResume() {
        super.onResume()
        presenter.atualizaListaNaTela()
    }

    fun configuraFabAddEvento() {
        binding.fabAddEvento.setOnClickListener {
            // quando vc clicar no fab tem que abrir um modal, lembra que eu falei que algumas coisas ficam
            // na activity?

            // mostrar um modal é responsabilidade da tela, agora o que vai ser feito quando clicar
            // no botão salvar não, isso sim pode ir pro presenter.

            val fragmentModalSalvar = FragmentModalSalvar(quandoClicarNoSalvar = { evento ->
                presenter.quandoClicaNoSalvar(evento)
            })
            fragmentModalSalvar.show(supportFragmentManager, null)

        }
    }

    fun mostraListaDeEventos(listaDeEventos: List<Evento>) {
        // to na tela, como eu mostro essa lista?

        eventoAdapter.atualiza(listaDeEventos)
    }

    // aqui a mesma coisa, o adapter é o cara responsavel por adaptar um Evento pra mostra na tela
    // então isso é responsa da tela
    fun configuraRecyclerViewEvento() {
        // primeira coisa, layout manager
        binding.recyclerViewEvento.layoutManager = LinearLayoutManager(this)

        // segunda coisa, criar o adapter
        eventoAdapter = TelaInicialAdapter(quandoEstiverSelecionado = { evento, taSelecionado ->
            presenter.quandoClicarNoRadio(evento, taSelecionado)
        }, quandoFizerCliqueCurto = { evento ->
            val intent = Intent(this, InformacoesDoEventoActivity::class.java)
            intent.putExtra(InformacoesDoEventoActivity.EXTRA_EVENTO_RECEBIDO_ID, evento.id)
            startActivity(intent)
        }, quandoFizerCliqueLongo = { evento, itemClicado ->

            showPopupMenu(itemClicado, evento)
        })

        // terceira coisa, grudar o adapter naquele recycler
        binding.recyclerViewEvento.adapter = eventoAdapter
    }

    fun showPopupMenu(itemClicado: View, evento: Evento) {
        val popup = PopupMenu(this, itemClicado)
        popup.menuInflater.inflate(R.menu.menu_excluir_editar, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            presenter.quandoClicarNoMenu(menuItem, evento)
            return@setOnMenuItemClickListener true
        }
        popup.show()
    }


    fun mostraModalDeEditar(eventoParaEditar: Evento) {
        val pacotinho = Bundle()
        pacotinho.putParcelable(FragmentModalSalvar.EXTRA_EVENTO_EDITAR, eventoParaEditar)

        val fragment = FragmentModalSalvar(quandoClicarNoSalvar = { eventoEditado ->
            presenter.quandoClicaNoEditar(eventoEditado)
        })
        fragment.arguments = pacotinho
        fragment.show(supportFragmentManager, null)
    }


}
