package com.nevesrafael.afazer.telas.cria_evento

import com.google.android.material.datepicker.MaterialDatePicker
import com.nevesrafael.afazer.formatador.FormatadorData
import com.nevesrafael.afazer.model.Evento
import java.util.*


class CriaEventoPresenter(val tela: CriaEventoFragment) {

    private var taEditando: Boolean = false
    private var eventoParaEditar: Evento? = null
    private var enderecoSelecionado: String? = null
    private var latitudeSelecionada: Double? = null
    private var longitudeSelecionada: Double? = null
    private var dataSelecionada: String? = null
    private var horarioSelecionado: String? = null
    private var horaPadrao: Int = 12
    private var minutoPadrao: Int = 10

    fun verificaSeEstaEditando(eventoCarregado: Evento?) {
        eventoParaEditar = eventoCarregado

        if (eventoParaEditar != null) {
            taEditando = true
            enderecoSelecionado = eventoParaEditar?.endereco
            latitudeSelecionada = eventoParaEditar?.latitude
            longitudeSelecionada = eventoParaEditar?.longitude
            dataSelecionada = eventoParaEditar?.data
            horarioSelecionado = eventoParaEditar?.horario

            tela.preencheDadosNaTelaParaEdicao(eventoParaEditar)
        }
    }

    fun verificaHorarioSelecionado() {

        if (horarioSelecionado != null) {
            val horaDividida = horarioSelecionado!!.split(":")

            horaPadrao = horaDividida[0].toInt()
            minutoPadrao = horaDividida[1].toInt()
        }
        tela.mostraSeletorDeHora(horaPadrao, minutoPadrao)
    }

    fun quandoSelecionaHora(pickerHour: Int, pickerMinute: Int) {

        horaPadrao = pickerHour

        minutoPadrao = pickerMinute

        horarioSelecionado =
            String.format(Locale.getDefault(), "%02d:%02d", horaPadrao, minutoPadrao)

        tela.escreveHorarioNaTela(horarioSelecionado!!)
    }

    fun verificaDataSelecionada() {

        var dataPadrao = MaterialDatePicker.todayInUtcMilliseconds() // por padrão hoje

        if (dataSelecionada != null) {
            val dataComoDate = FormatadorData.stringParaDate(dataSelecionada!!, "dd/MM/yyyy")

            if (dataComoDate != null) {
                dataPadrao = dataComoDate.time // se tiver alguma data, converter
            }
        }
        tela.mostraSeletorData(dataPadrao)
    }

    fun quandoSelecionaData(dataEmMilisegundos: Long) {

        dataSelecionada = FormatadorData.formataMilliEmData(dataEmMilisegundos)

        tela.escreveDataNaTela(dataSelecionada)

    }

    fun getLatitude(): Double? {
        return latitudeSelecionada
    }

    fun getLongitude(): Double? {
        return longitudeSelecionada
    }

    fun getEndereco(): String? {
        return enderecoSelecionado
    }

    fun verificaParaSalvarOuEditar(eventoDigitado: String, descricaoDigitada: String) {

        val evento: Evento

        if (taEditando == false) {
            evento = criaNovoEvento(eventoDigitado, descricaoDigitada)
        } else {
            evento = alterarEventoParaEditar(eventoDigitado, descricaoDigitada)
        }

        tela.salvarOuEditar(evento)
    }

    private fun criaNovoEvento(eventoDigitado: String, descricaoDigitada: String): Evento {
        return Evento(
            id = 0,
            data = dataSelecionada,
            endereco = enderecoSelecionado,
            latitude = latitudeSelecionada,
            longitude = longitudeSelecionada,
            evento = eventoDigitado,
            descricao = descricaoDigitada,
            finalizado = false,
            horario = horarioSelecionado
        )
    }

    private fun alterarEventoParaEditar(eventoDigitado: String, descricaoDigitada: String): Evento {
        eventoParaEditar?.apply {
            evento = eventoDigitado
            descricao = descricaoDigitada
            endereco = enderecoSelecionado
            latitude = latitudeSelecionada
            longitude = longitudeSelecionada
            data = dataSelecionada
            horario = horarioSelecionado
        }

        return eventoParaEditar!!
    }

    fun quandoVoltaDaTelaMapa(endereco: String?, longitude: Double?, latitude: Double?) {
        enderecoSelecionado = endereco
        longitudeSelecionada = longitude
        latitudeSelecionada = latitude

        tela.mostraLocalNoCampo(enderecoSelecionado)
    }


}