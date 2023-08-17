package com.septianen.imagemachine.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.septianen.imagemachine.constant.Constant.Table
import com.septianen.imagemachine.model.Image
import com.septianen.imagemachine.model.Machine

@Dao
interface MachineDao {

    // Machine
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertMachine(machine: Machine): Long
    @Query("DELETE FROM ${Table.MACHINE} WHERE id = :machineId")
    fun deleteMachine(machineId: Long)
    @Query("SELECT * FROM ${Table.MACHINE} ORDER BY type ASC")
    fun getMachinesByTypeAsc(): List<Machine>?
    @Query("SELECT * FROM ${Table.MACHINE} ORDER BY type DESC")
    fun getMachinesByTypeDesc(): List<Machine>?
    @Query("SELECT * FROM ${Table.MACHINE} ORDER BY name ASC")
    fun getMachinesByNameAsc(): List<Machine>?
    @Query("SELECT * FROM ${Table.MACHINE} ORDER BY name DESC")
    fun getMachinesByNameDesc(): List<Machine>?
    @Query("SELECT * FROM ${Table.MACHINE} WHERE qrNumber = :number")
    fun getMachineByNumber(number: Int): Machine?


    // Image
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertImages(images: List<Image>): List<Long>
    @Query("DELETE FROM image WHERE imagePath = :image")
    fun deleteImage(image: String)
    @Query("DELETE FROM image WHERE imagePath in (:images)")
    fun deleteImagePaths(images: List<String>)
    @Delete
    fun deleteImages(images: List<Image>)
    @Query("SELECT * FROM ${Table.IMAGE} WHERE machineId = :id")
    fun getImages(id: Long): List<Image>
    @Query("DELETE FROM ${Table.IMAGE} WHERE machineId = :id")
    fun deleteAllImage(id: Long)

    @Transaction
    fun updateImages(machineId: Long, images: List<Image>) {
        deleteAllImage(machineId)
        upsertImages(images)
    }

    @Transaction
    fun deleteMachineData(machineId: Long) {
        deleteAllImage(machineId)
        deleteMachine(machineId)
    }
}