package com.septianen.imagemachine.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.septianen.imagemachine.constant.Constant
import com.septianen.imagemachine.model.Image
import com.septianen.imagemachine.model.Machine

@Database(
    entities = [Machine::class, Image::class],
    version = 1,
    exportSchema = false
)
abstract class MachineDB: RoomDatabase() {
    abstract fun machineDao(): MachineDao

    companion object {
        private var INSTANCE: MachineDB? = null

        fun getInstance(context: Context): MachineDB {
            if (INSTANCE == null) {
                synchronized(RoomDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MachineDB::class.java, Constant.DB.MACHINE
                    )
                        .fallbackToDestructiveMigrationOnDowngrade()
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE!!
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}