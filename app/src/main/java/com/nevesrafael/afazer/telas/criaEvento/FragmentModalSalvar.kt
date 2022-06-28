package com.nevesrafael.afazer.telas.criaEvento

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.nevesrafael.afazer.formatador.FormatadorData
import com.nevesrafael.afazer.databinding.FragmentModalSalvarBinding
import com.nevesrafael.afazer.model.Evento
import com.nevesrafael.afazer.telas.mapa.SelecionaEnderecoActivity
import java.text.SimpleDateFormat
import java.util.*

class FragmentModalSalvar(val quandoClicarNoSalvar: (Evento) -> Unit) :
    BottomSheetDialogFragment() {

    private lateinit var binding: FragmentModalSalvarBinding
    private var endereco: String? = null
    private var latitude: Double? = null
    private var longitude: Double? = null
    private var data: String? = null
    private var hora: String? = null
    private var horaSelecionada: Int = 12
    private var minutoSelecionado: Int = 10

    private var eventoParaEditar: Evento? = null
    private var taEditando: Boolean = false

    companion object {
        const val REQUEST_CODE_ADDRESS = 123
        const val EXTRA_EVENTO_LATITUDE = "extra.evento.latitude"
        const val EXTRA_EVENTO_LONGITUDE = "extra.evento.longitude"
        const val EXTRA_EVENTO_ENDERECO = "extra.evento.endereco"
        const val EXTRA_EVENTO_ID_EDITAR = "extra.evento.id.editar"

    }


    // imagina que esse é o OnCreate parte 1
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModalSalvarBinding.inflate(inflater, container, false)
        return binding.root
    }


    // imagina que esse é o OnCreate parte 2
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        configuraBotaoSalvar()
        verificaSeEstaEditando()
        configuraBotaoLocal()
        configuraBotaoData()
        configuraBotaoHora()
    }

    private fun verificaSeEstaEditando() {
        val pacotinho = arguments

        eventoParaEditar = pacotinho?.getParcelable(EXTRA_EVENTO_ID_EDITAR)

        if (eventoParaEditar != null) {
            taEditando = true
            binding.evento.setText(eventoParaEditar?.evento)
            binding.descricao.setText(eventoParaEditar?.descricao)
            endereco = eventoParaEditar?.endereco
            binding.local.setText(this.endereco, TextView.BufferType.EDITABLE)
            latitude = eventoParaEditar?.latitude
            longitude = eventoParaEditar?.longitude
            data = eventoParaEditar?.data
            binding.data.setText(data, TextView.BufferType.EDITABLE)
            hora = eventoParaEditar?.horario
            binding.horario.setText(hora, TextView.BufferType.EDITABLE)
        }
    }

    private fun configuraBotaoHora() {
        binding.horario.setOnClickListener {

            if(hora != null){
                val horaDividida = hora!!.split(":")

                horaSelecionada = horaDividida[0].toInt()
                minutoSelecionado = horaDividida[1].toInt()
            }

            val picker = MaterialTimePicker.Builder()
                .setTimeFormat(TimeFormat.CLOCK_12H)
                .setHour(horaSelecionada)
                .setMinute(minutoSelecionado)
                .setTitleText("Selecione o horário")
                .build()

            picker.addOnPositiveButtonClickListener {
                horaSelecionada = picker.hour
                minutoSelecionado = picker.minute
                hora = "$horaSelecionada:$minutoSelecionado"

                binding.horario.setText(hora, TextView.BufferType.EDITABLE)

            }

            picker.show(parentFragmentManager, null)
        }
    }

    private fun configuraBotaoData() {
        binding.data.setOnClickListener {

            var dataSelecionada = MaterialDatePicker.todayInUtcMilliseconds() // por padrão hoje

            if (data != null) {
                val dataComoDate = FormatadorData.stringParaDate(data!!, "dd/MM/yyyy")

                if (dataComoDate != null) {
                    dataSelecionada = dataComoDate.time // se tiver alguma data, converter
                }
            }

            // limitando para datas a partir de hj
            val limitesDoCalendario = CalendarConstraints.Builder()
                .setStart(MaterialDatePicker.todayInUtcMilliseconds())
                .setValidator(DateValidatorPointForward.now())
                .build()

            val calendario = MaterialDatePicker.Builder
                .datePicker()
                .setTitleText("Selecione a data")
                .setSelection(dataSelecionada)
                .setCalendarConstraints(limitesDoCalendario) // colocando o limite
                .build()

            calendario.addOnPositiveButtonClickListener { dataEmMilisegundos ->

                formataMilliEmData(dataEmMilisegundos)
            }
            calendario.show(parentFragmentManager, null)
        }
    }

    private fun formataMilliEmData(dataEmMilisegundos: Long) {
        val calendario = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendario.timeInMillis = dataEmMilisegundos

        val formatador = SimpleDateFormat("dd/MM/yyyy")
        data = formatador.format(calendario.time)

        binding.data.setText(data, TextView.BufferType.EDITABLE)
    }

    private fun configuraBotaoLocal() {
        binding.local.setOnClickListener {
            val intent = Intent(context, SelecionaEnderecoActivity::class.java)
            intent.putExtra(EXTRA_EVENTO_LONGITUDE, longitude)
            intent.putExtra(EXTRA_EVENTO_LATITUDE, latitude)
            intent.putExtra(EXTRA_EVENTO_ENDERECO, endereco)
            startActivityForResult(intent, REQUEST_CODE_ADDRESS)
        }
    }

    private fun configuraBotaoSalvar() {
        binding.botaoSalvar.setOnClickListener {

            val evento: Evento
            if (taEditando == false) {
                evento = criarEvento()
            } else {
                evento = alterarEventoParaEditar()
            }

            quandoClicarNoSalvar(evento)
            dismissAllowingStateLoss()
        }
    }

    private fun alterarEventoParaEditar(): Evento {
        eventoParaEditar?.apply {
            evento = binding.evento.text.toString()
            descricao = binding.descricao.text.toString()
            endereco = this@FragmentModalSalvar.endereco
            latitude = this@FragmentModalSalvar.latitude
            longitude = this@FragmentModalSalvar.longitude
            data = this@FragmentModalSalvar.data
            horario = this@FragmentModalSalvar.hora
        }

        return eventoParaEditar!!
    }

    private fun criarEvento(): Evento {
        return Evento(
            id = 0,
            data = data,
            endereco = this.endereco,
            latitude = this.latitude,
            longitude = this.longitude,
            evento = binding.evento.text.toString(),
            descricao = binding.descricao.text.toString(),
            finalizado = false,
            horario = hora
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_ADDRESS && resultCode == Activity.RESULT_OK) {
            endereco = data?.getStringExtra(EXTRA_EVENTO_ENDERECO)
            latitude = data?.getDoubleExtra(EXTRA_EVENTO_LATITUDE, 0.0)
            longitude = data?.getDoubleExtra(EXTRA_EVENTO_LONGITUDE, 0.0)

            binding.local.setText(endereco, TextView.BufferType.EDITABLE)
        }


    }
}





