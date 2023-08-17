package com.septianen.imagemachine.repository

import com.septianen.imagemachine.constant.Constant
import com.septianen.imagemachine.db.MachineDao
import com.septianen.imagemachine.model.Image
import com.septianen.imagemachine.model.Machine
import javax.inject.Inject

class MachineRepository @Inject constructor(private val machineDao: MachineDao) {

    fun getMachines(
        category: Int? = null,
        sort: Int? = null
    ): List<Machine>? {

        return if (category == Constant.Sort.NAME && sort == Constant.Sort.ASC)
            machineDao.getMachinesByNameAsc()
        else if (category == Constant.Sort.NAME && sort == Constant.Sort.DSC)
            machineDao.getMachinesByNameDesc()
        else if (category == Constant.Sort.TYPE && sort == Constant.Sort.ASC)
            machineDao.getMachinesByTypeAsc()
        else if (category == Constant.Sort.TYPE && sort == Constant.Sort.DSC)
            machineDao.getMachinesByTypeDesc()
        else
            machineDao.getMachinesByNameAsc()
    }
    fun getMachineByNumber(number: Int) = machineDao.getMachineByNumber(number)
    fun upsertMachine(machine: Machine) = machineDao.upsertMachine(machine)
    fun deleteMachineData(machineId: Long) = machineDao.deleteMachineData(machineId)

    fun getImages(machineId: Long) = machineDao.getImages(machineId)
    fun updateImages(machineId: Long, images: List<Image>) = machineDao.updateImages(machineId, images)
}