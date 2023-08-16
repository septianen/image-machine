package com.septianen.imagemachine.di

import android.content.Context
import androidx.room.Room
import com.septianen.imagemachine.constant.Constant
import com.septianen.imagemachine.db.MachineDB
import com.septianen.imagemachine.db.MachineDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Provides
    fun provideMachineDao(machineDB: MachineDB): MachineDao {
        return machineDB.machineDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): MachineDB {
        return Room.databaseBuilder(
            appContext,
            MachineDB::class.java,
            Constant.DB.MACHINE
        ).build()
    }
}