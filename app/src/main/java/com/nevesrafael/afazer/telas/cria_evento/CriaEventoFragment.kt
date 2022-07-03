package com.nevesrafael.afazer.telas.cria_evento

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
import com.nevesrafael.afazer.databinding.FragmentModalSalvarBinding
import com.nevesrafael.afazer.model.Evento
import com.nevesrafael.afazer.telas.seleciona_endereco.SelecionaEnderecoActivity

class CriaEventoFragment(val quandoClicarNoSalvar: (Evento) -> Unit) :
    BottomSheetDialogFragment() {

    private lateinit var binding: FragmentModalSalvarBinding
    private lateinit var presenter: CriaEventoPresenter

    companion object {
        const val REQUEST_CODE_ADDRESS = 123
        const val EXTRA_EVENTO_LATITUDE = "extra.evento.latitude"
        const val EXTRA_EVENTO_LONGITUDE = "extra.evento.longitude"
        const val EXTRA_EVENTO_ENDERECO = "extra.evento.endereco"
        const val EXTRA_EVENTO_EDITAR = "extra.evento.id.editar"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentModalSalvarBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = CriaEventoPresenter(this)

        botaoSalvar()

        configuraBotaoLocal()
        configuraData()
        configuraHorario()
        carregaDadosParaEditar()
    }

    private fun carregaDadosParaEditar() {
        val pacotinho = arguments
        val eventoCarregado: Evento? = pacotinho?.getParcelable(EXTRA_EVENTO_EDITAR)
        presenter.verificaSeEstaEditando(eventoCarregado)
    }

    fun preencheDadosNaTelaParaEdicao(eventoParaEditar: Evento?) {
        binding.evento.setText(eventoParaEditar?.evento)
        binding.descricao.setText(eventoParaEditar?.descricao)
        binding.local.setText(eventoParaEditar?.endereco, TextView.BufferType.EDITABLE)
        binding.data.setText(eventoParaEditar?.data, TextView.BufferType.EDITABLE)
        binding.horario.setText(eventoParaEditar?.horario, TextView.BufferType.EDITABLE)
    }

    fun configuraHorario() {
        binding.horario.setOnClickListener {

            presenter.verificaHorarioSelecionado()
        }
    }

    fun mostraSeletorDeHora(horaPadrao: Int, minutoPadrao: Int) {
        val picker = MaterialTimePicker.Builder()

            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(horaPadrao)
            .setMinute(minutoPadrao)
            .setTitleText("Selecione o horário")
            .build()

        picker.addOnPositiveButtonClickListener {

            presenter.quandoSelecionaHora(picker.hour, picker.minute)
        }

        picker.show(parentFragmentManager, null)
    }

    fun escreveHorarioNaTela(horarioSelecionado: String) {
        binding.horario.setText(horarioSelecionado, TextView.BufferType.EDITABLE)
    }

    private fun configuraData() {
        binding.data.setOnClickListener {
            presenter.verificaDataSelecionada()
        }
    }

    fun mostraSeletorData(dataPadrao: Long) {
        // limitando para datas a partir de hj
        val limitesDoCalendario = CalendarConstraints.Builder()
            .setStart(MaterialDatePicker.todayInUtcMilliseconds())
            .setValidator(DateValidatorPointForward.now())
            .build()

        val calendario = MaterialDatePicker.Builder
            .datePicker()
            .setTitleText("Selecione a data")
            .setSelection(dataPadrao)
            .setCalendarConstraints(limitesDoCalendario) // colocando o limite
            .build()

        calendario.addOnPositiveButtonClickListener {
            presenter.quandoSelecionaData(it)
        }
        calendario.show(parentFragmentManager, null)
    }

    fun escreveDataNaTela(dataSelecionada: String?) {
        binding.data.setText(dataSelecionada, TextView.BufferType.EDITABLE)
    }

    private fun configuraBotaoLocal() {
        binding.local.setOnClickListener {
            val intent = Intent(context, SelecionaEnderecoActivity::class.java)
            intent.putExtra(EXTRA_EVENTO_LONGITUDE, presenter.getLongitude())
            intent.putExtra(EXTRA_EVENTO_LATITUDE, presenter.getLatitude())
            intent.putExtra(EXTRA_EVENTO_ENDERECO, presenter.getEndereco())
            startActivityForResult(intent, REQUEST_CODE_ADDRESS)
        }
    }

    private fun botaoSalvar() {
        binding.botaoSalvar.setOnClickListener {

            val eventoDigitado = binding.evento.text.toString()
            val descricaoDigitada = binding.descricao.text.toString()
            presenter.verificaParaSalvarOuEditar(eventoDigitado, descricaoDigitada)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_ADDRESS && resultCode == Activity.RESULT_OK) {
            val endereco = data?.getStringExtra(EXTRA_EVENTO_ENDERECO)
            val latitude = data?.getDoubleExtra(EXTRA_EVENTO_LATITUDE, 0.0)
            val longitude = data?.getDoubleExtra(EXTRA_EVENTO_LONGITUDE, 0.0)

            presenter.quandoVoltaDaTelaMapa(endereco, longitude, latitude)
        }
    }

    fun mostraLocalNoCampo(endereco: String?) {
        binding.local.setText(endereco, TextView.BufferType.EDITABLE)
    }

    fun salvarOuEditar(evento: Evento) {
        quandoClicarNoSalvar(evento)
        dismissAllowingStateLoss()
    }


}





