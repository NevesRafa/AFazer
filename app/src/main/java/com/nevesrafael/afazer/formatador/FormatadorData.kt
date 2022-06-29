package com.nevesrafael.afazer.formatador

import java.text.SimpleDateFormat
import java.util.*

object FormatadorData {

    fun stringParaDate(dataComoString: String, formatoDaData: String): Date? {
        val formatador = SimpleDateFormat(formatoDaData)
        return formatador.parse(dataComoString)
    }

    fun formataMilliEmData(dataEmMilisegundos: Long): String? {
        val calendario = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendario.timeInMillis = dataEmMilisegundos

        val formatador = SimpleDateFormat("dd/MM/yyyy")
        return formatador.format(calendario.time)
    }
}