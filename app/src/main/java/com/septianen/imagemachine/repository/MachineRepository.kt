package com.septianen.imagemachine.repository

import com.septianen.imagemachine.db.MachineDao
import com.septianen.imagemachine.model.Image
import com.septianen.imagemachine.model.Machine
import javax.inject.Inject

class MachineRepository @Inject constructor(private val machineDao: MachineDao) {

    fun getMachines() = machineDao.getMachines()
    fun upsertMachine(machine: Machine) = machineDao.upsertMachine(machine)
    fun deleteMachine(machine: Machine) = machineDao.deleteMachine(machine)

    fun getImages(machineId: Long) = machineDao.getImages(machineId)
    fun upsertImages(images: List<Image>) = machineDao.upsertImages(images)
    fun updateImages(machineId: Long, images: List<Image>) = machineDao.updateImages(machineId, images)
    fun deleteImage(image: String) = machineDao.deleteImage(image)
    fun deleteImages(images: List<Image>) = machineDao.deleteImages(images)
}