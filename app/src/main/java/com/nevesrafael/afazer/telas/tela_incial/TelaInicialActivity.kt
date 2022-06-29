package com.nevesrafael.afazer.telas.tela_incial

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.recyclerview.widget.LinearLayoutManager
import com.nevesrafael.afazer.telas.cria_evento.FragmentModalSalvar
import com.nevesrafael.afazer.R
import com.nevesrafael.afazer.database.AppDatabase
import com.nevesrafael.afazer.database.EventoDao
import com.nevesrafael.afazer.databinding.ActivityTelaInicialBinding
import com.nevesrafael.afazer.model.Evento
import com.nevesrafael.afazer.telas.informacoes_evento.InformacoesDoEventoActivity

class TelaInicialActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTelaInicialBinding
    private lateinit var eventoAdapter: TelaInicialAdapter
    private lateinit var eventoDao: EventoDao


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaInicialBinding.inflate(layoutInflater)
        eventoDao = AppDatabase.instancia(this).eventoDao()
        setContentView(binding.root)
        configuraFabAddEvento()
        configuraRecyclerViewEvento()
    }


    override fun onResume() {
        super.onResume()
        eventoAdapter.atualiza(eventoDao.buscaTodos())
    }

    private fun configuraRecyclerViewEvento() {
        eventoAdapter = TelaInicialAdapter(quandoEstiverSelecionado = { evento, taSelecionado ->
            evento.finalizado = taSelecionado
            eventoDao.altera(evento)
            eventoAdapter.atualiza(eventoDao.buscaTodos())
        }, quandoFizerCliqueCurto = { evento ->
            val intent = Intent(
                this, InformacoesDoEventoActivity::class.java
            )

            intent.putExtra(InformacoesDoEventoActivity.EXTRA_EVENTO_RECEBIDO_ID, evento.id)
            startActivity(intent)

        }, quandoFizerCliqueLongo = { evento, itemClicado ->
            showPopupMenu(itemClicado, evento)
        })
        binding.recyclerViewEvento.adapter = eventoAdapter
        binding.recyclerViewEvento.layoutManager = LinearLayoutManager(this)
    }

    private fun showPopupMenu(itemClicado: View, evento: Evento) {
        val popup = PopupMenu(this, itemClicado)
        popup.menuInflater.inflate(R.menu.menu_excluir_editar, popup.menu)

        popup.setOnMenuItemClickListener { menuItem ->
            if (menuItem.itemId == R.id.menu_excluir) {
                eventoDao.remove(evento)
                eventoAdapter.atualiza(eventoDao.buscaTodos())
            } else if (menuItem.itemId == R.id.menu_editar) {

                val pacotinho = Bundle()
                pacotinho.putParcelable(FragmentModalSalvar.EXTRA_EVENTO_ID_EDITAR, evento)

                val fragment = FragmentModalSalvar(quandoClicarNoSalvar = { eventoEditado ->
                    eventoDao.altera(eventoEditado)
                    eventoAdapter.atualiza(eventoDao.buscaTodos())
                })
                fragment.arguments = pacotinho
                fragment.show(supportFragmentManager, null)
            }
            return@setOnMenuItemClickListener true
        }
        popup.show()
    }

    private fun configuraFabAddEvento() {
        binding.fabAddEvento.setOnClickListener {
            val fragmentModalSalvar = FragmentModalSalvar(quandoClicarNoSalvar = { evento ->
                eventoDao.salvar(evento)
                eventoAdapter.atualiza(eventoDao.buscaTodos())
            })

            fragmentModalSalvar.show(supportFragmentManager, null)
        }
    }


}
