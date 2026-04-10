package com.fsales.app.rumo.core.data.room.converter

import androidx.room.TypeConverter
import com.fsales.app.rumo.core.data.room.entity.enums.CategoriaGasto
import com.fsales.app.rumo.core.data.room.entity.enums.PrioridadeSonho
import com.fsales.app.rumo.core.data.room.entity.enums.StatusSonho
import com.fsales.app.rumo.core.data.room.entity.enums.TipoGanho
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate

object AppTypeConverters {
    @TypeConverter
    fun bigDecimalToString(value: BigDecimal?): String? = value?.toPlainString()

    @TypeConverter
    fun stringToBigDecimal(value: String?): BigDecimal? = value?.let(::BigDecimal)

    @TypeConverter
    fun localDateToString(value: LocalDate?): String? = value?.toString()

    @TypeConverter
    fun stringToLocalDate(value: String?): LocalDate? = value?.let(LocalDate::parse)

    @TypeConverter
    fun instantToLong(value: Instant?): Long? = value?.toEpochMilli()

    @TypeConverter
    fun longToInstant(value: Long?): Instant? = value?.let(Instant::ofEpochMilli)

    @TypeConverter
    fun tipoGanhoToString(value: TipoGanho?): String? = value?.name

    @TypeConverter
    fun stringToTipoGanho(value: String?): TipoGanho? = value?.let(TipoGanho::valueOf)

    @TypeConverter
    fun categoriaGastoToString(value: CategoriaGasto?): String? = value?.name

    @TypeConverter
    fun stringToCategoriaGasto(value: String?): CategoriaGasto? = value?.let(CategoriaGasto::valueOf)

    @TypeConverter
    fun prioridadeSonhoToString(value: PrioridadeSonho?): String? = value?.name

    @TypeConverter
    fun stringToPrioridadeSonho(value: String?): PrioridadeSonho? = value?.let(PrioridadeSonho::valueOf)

    @TypeConverter
    fun statusSonhoToString(value: StatusSonho?): String? = value?.name

    @TypeConverter
    fun stringToStatusSonho(value: String?): StatusSonho? = value?.let(StatusSonho::valueOf)
}