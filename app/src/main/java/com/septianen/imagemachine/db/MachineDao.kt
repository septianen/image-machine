package com.septianen.imagemachine.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.septianen.imagemachine.constant.Constant.Table
import com.septianen.imagemachine.model.Image
import com.septianen.imagemachine.model.Machine

@Dao
interface MachineDao {

    // Machine
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertMachine(machine: Machine): Long
    @Delete
    fun deleteMachine(machine: Machine)
    @Query("SELECT * FROM ${Table.MACHINE}")
    fun getMachines(): List<Machine>?

    // Image
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertImage(image: Image): Long
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsertImages(images: List<Image>): List<Long>
    @Query("DELETE FROM image WHERE imagePath in (:images)")
    fun deleteImage(images: List<String>)
    @Query("SELECT imagePath FROM ${Table.IMAGE} WHERE machineId = :id")
    fun getImages(id: Long): List<String>
}