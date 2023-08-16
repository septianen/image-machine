package com.septianen.imagemachine.repository

import com.septianen.imagemachine.db.MachineDao
import com.septianen.imagemachine.model.Machine
import javax.inject.Inject

class MachineRepository @Inject constructor(private val machineDao: MachineDao) {

    suspend fun getMachines() = machineDao.getMachines()

    fun upsertMachine(machine: Machine) = machineDao.upsertMachine(machine)
}