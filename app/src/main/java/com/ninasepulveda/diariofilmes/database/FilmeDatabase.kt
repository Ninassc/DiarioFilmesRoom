package com.ninasepulveda.diariofilmes.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [FilmeEntity::class], version = 1, exportSchema = false)
abstract  class  FilmeDatabase : RoomDatabase() {

    abstract fun filmeDao() : FilmeDao

    companion object {
        @Volatile private var INSTANCE : FilmeDatabase? = null

        fun getInstance(context: Context) : FilmeDatabase {
            return INSTANCE ?: synchronized(this) {
                Room.databaseBuilder(
                    context.applicationContext,
                    FilmeDatabase::class.java,
                    "filme.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}