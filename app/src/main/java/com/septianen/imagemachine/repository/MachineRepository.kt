package com.septianen.imagemachine.repository

import com.septianen.imagemachine.constant.Constant
import com.septianen.imagemachine.db.MachineDao
import com.septianen.imagemachine.model.Image
import com.septianen.imagemachine.model.Machine
import java.util.jar.Attributes.Name
import javax.inject.Inject

class MachineRepository @Inject constructor(private val machineDao: MachineDao) {

    fun getMachines(
        category: Int? = null,
        sort: Int? = null
    ): List<Machine>?  {

        val sorted = if (category == Constant.Sort.NAME && sort == Constant.Sort.ASC)
            machineDao.getMachinesByNameAsc()
        else if (category == Constant.Sort.NAME && sort == Constant.Sort.DSC)
            machineDao.getMachinesByNameDesc()
        else if (category == Constant.Sort.TYPE && sort == Constant.Sort.ASC)
            machineDao.getMachinesByTypeAsc()
        else if (category == Constant.Sort.TYPE && sort == Constant.Sort.DSC)
            machineDao.getMachinesByTypeDesc()
        else
            machineDao.getMachinesByNameAsc()

        return sorted
    }
    fun upsertMachine(machine: Machine) = machineDao.upsertMachine(machine)
    fun deleteMachine(machine: Machine) = machineDao.deleteMachine(machine)

    fun getImages(machineId: Long) = machineDao.getImages(machineId)
    fun upsertImages(images: List<Image>) = machineDao.upsertImages(images)
    fun updateImages(machineId: Long, images: List<Image>) = machineDao.updateImages(machineId, images)
    fun deleteImage(image: String) = machineDao.deleteImage(image)
    fun deleteImages(images: List<Image>) = machineDao.deleteImages(images)
}