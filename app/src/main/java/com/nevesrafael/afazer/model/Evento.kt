package com.nevesrafael.afazer.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Evento(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var evento : String,
    var data : String,
    var endereco : String?,
    var latitude : Double?,
    var longitude : Double?,
    var descricao : String?,
    var finalizado : Boolean

): Parcelable
