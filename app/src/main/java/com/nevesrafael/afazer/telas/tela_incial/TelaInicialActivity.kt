package com.nevesrafael.afazer.telas.tela_incial

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.nevesrafael.afazer.FragmentModalSalvar
import com.nevesrafael.afazer.telas.informacoesDoEvento.InformacoesDoEventoActivity
import com.nevesrafael.afazer.database.AppDatabase
import com.nevesrafael.afazer.database.EventoDao
import com.nevesrafael.afazer.databinding.ActivityTelaInicialBinding

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

            intent.putExtra(InformacoesDoEventoActivity.EXTRA_EVENTO_RECEBIDO_ID,evento.id)
            startActivity(intent)

        })
        binding.recyclerViewEvento.adapter = eventoAdapter
        binding.recyclerViewEvento.layoutManager = LinearLayoutManager(this)
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
