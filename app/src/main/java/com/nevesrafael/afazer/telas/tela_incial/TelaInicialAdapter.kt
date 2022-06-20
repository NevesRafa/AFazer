package com.nevesrafael.afazer.telas.tela_incial

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.nevesrafael.afazer.R
import com.nevesrafael.afazer.databinding.ItemEventoBinding
import com.nevesrafael.afazer.model.Evento

class TelaInicialAdapter(
    val quandoEstiverSelecionado: (Evento, Boolean) -> Unit,
    //val quandoFizerCliqueLongo: (Evento) -> Boolean,
    val quandoFizerCliqueCurto: (Evento) -> Unit
) :
    RecyclerView.Adapter<TelaInicialViewHolder>() {

    private val eventos = mutableListOf<Evento>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TelaInicialViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemEventoBinding.inflate(inflater, parent, false)
        return TelaInicialViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TelaInicialViewHolder, position: Int) {
        val evento = eventos[position]
        holder.vincula(
            evento,
            quandoEstiverSelecionado,
            //quandoFizerCliqueLongo,
            quandoFizerCliqueCurto
        )
    }

    override fun getItemCount() = eventos.size

    fun atualiza(eventos: List<Evento>) {
        this.eventos.clear()
        this.eventos.addAll(eventos)
        notifyDataSetChanged()
    }
}

class TelaInicialViewHolder(val binding: ItemEventoBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun vincula(
        evento: Evento,
        quandoEstiverSelecionado: (Evento, Boolean) -> Unit,
        // quandoFizerCliqueLongo: (Evento) -> Boolean,
        quandoFizerCliqueCurto: (Evento) -> Unit
    ) {
        binding.checkboxEvento.text = evento.evento
        binding.checkboxEvento.isChecked = evento.finalizado

        if (evento.finalizado) {
            binding.checkboxEvento.setTextColor(ContextCompat.getColor(binding.root.context, android.R.color.darker_gray))
            binding.checkboxEvento.paintFlags = binding.checkboxEvento.paintFlags.or(Paint.STRIKE_THRU_TEXT_FLAG)
        } else {
            binding.checkboxEvento.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
            binding.checkboxEvento.paintFlags = binding.checkboxEvento.paintFlags.and(Paint.STRIKE_THRU_TEXT_FLAG.inv())
        }

        binding.checkboxEvento.setOnCheckedChangeListener { _, isChecked ->
            quandoEstiverSelecionado(evento, isChecked)
        }

//        binding.root.setOnLongClickListener {
//            quandoFizerCliqueLongo(evento)
//        }

        binding.root.setOnClickListener {
            quandoFizerCliqueCurto(evento)
        }
    }
}



