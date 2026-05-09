package com.fsales.app.smartcontact.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.fsales.app.smartcontact.data.local.converter.LocalDateConverter
import com.fsales.app.smartcontact.data.local.dao.ContatoDao
import com.fsales.app.smartcontact.data.local.entity.ContatoEntity

@Database(
    entities = [ContatoEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(LocalDateConverter::class)
abstract class SmartContactDatabase : RoomDatabase() {
    abstract fun contatoDao(): ContatoDao
}

