package com.nevesrafael.afazer

import java.text.SimpleDateFormat
import java.util.*

object FormatadorData {

    fun stringParaDate(dataComoString: String, formatoDaData: String): Date? {
        val formatador = SimpleDateFormat(formatoDaData)
        return formatador.parse(dataComoString)
    }
}