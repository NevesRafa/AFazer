package com.nevesrafael.afazer.database

import androidx.room.*
import com.nevesrafael.afazer.model.Evento

@Dao
interface EventoDao {

    @Query("SELECT * FROM Evento")
    fun buscaTodos(): List<Evento>

    @Insert
    fun salvar(evento: Evento)

    @Delete
    fun remove(evento: Evento)

    @Update
    fun altera(evento: Evento)

    @Query("SELECT * FROM Evento WHERE id = :id")
    fun buscaPorId(id: Int): Evento?
}