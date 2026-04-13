package com.fsales.app.rumo.core.config.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fsales.app.rumo.core.data.room.converter.AppTypeConverters
import com.fsales.app.rumo.core.data.room.dao.GanhoDao
import com.fsales.app.rumo.core.data.room.dao.GastoDao
import com.fsales.app.rumo.core.data.room.dao.SonhoDao
import com.fsales.app.rumo.core.data.room.entity.GanhoEntity
import com.fsales.app.rumo.core.data.room.entity.GastoEntity
import com.fsales.app.rumo.core.data.room.entity.SonhoEntity

@Database(
    entities = [GanhoEntity::class, GastoEntity::class, SonhoEntity::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ganhoDao(): GanhoDao
    abstract fun gastoDao(): GastoDao
    abstract fun sonhoDao(): SonhoDao
}