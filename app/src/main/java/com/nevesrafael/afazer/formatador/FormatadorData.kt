package com.nevesrafael.afazer.formatador

import java.text.SimpleDateFormat
import java.util.*

object FormatadorData {

    fun stringParaDate(dataComoString: String, formatoDaData: String): Date? {
        val formatador = SimpleDateFormat(formatoDaData)
        return formatador.parse(dataComoString)
    }
}