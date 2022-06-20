package com.nevesrafael.afazer.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Evento(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val evento : String,
    val data : String,
    val endereco : String?,
    val latitude : Double?,
    val longitude : Double?,
    val descricao : String?,
    var finalizado : Boolean

)
