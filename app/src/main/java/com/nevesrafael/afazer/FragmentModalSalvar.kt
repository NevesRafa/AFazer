package com.nevesrafael.afazer

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.nevesrafael.afazer.databinding.FragmentModalSalvarBinding
import com.nevesrafael.afazer.model.Evento

class FragmentModalSalvar(val quandoClicarNoSalvar: (Evento) -> Unit) :
    BottomSheetDialogFragment() {

    private lateinit var binding: FragmentModalSalvarBinding
    private var endereco: String? = null
    private var latitude: Double? = null
    private var longitude: Double? = null

    companion object {
        const val REQUEST_CODE_ADDRESS = 123
        const val EXTRA_EVENTO_LATITUDE = "extra.evento.latitude"
        const val EXTRA_EVENTO_LONGITUDE = "extra.evento.longitude"
        const val EXTRA_EVENTO_ENDERECO = "extra.evento.endereco"
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

        binding.botaoSalvar.setOnClickListener {
            val evento = Evento(
                id = 0,
                data = "",
                endereco = this.endereco,
                latitude = this.latitude,
                longitude = this.longitude,
                evento = binding.evento.text.toString(),
                descricao = binding.descricao.text.toString(),
                finalizado = false
            )

            quandoClicarNoSalvar(evento)
            dismissAllowingStateLoss()
        }

        binding.botaoLocal.setOnClickListener {
            val intent = Intent(context, SelecionaEnderecoActivity::class.java)
            startActivityForResult(intent, REQUEST_CODE_ADDRESS)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_ADDRESS && resultCode == Activity.RESULT_OK) {
            endereco = data?.getStringExtra(EXTRA_EVENTO_ENDERECO)
            latitude = data?.getDoubleExtra(EXTRA_EVENTO_LATITUDE, 0.0)
            longitude = data?.getDoubleExtra(EXTRA_EVENTO_LONGITUDE, 0.0)
        }


    }
}





