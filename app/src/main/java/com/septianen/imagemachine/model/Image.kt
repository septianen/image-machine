package com.septianen.imagemachine.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.septianen.imagemachine.constant.Constant

@Entity(tableName = Constant.Table.IMAGE)
data class Image(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var machineId: Long? = null,
    var imagePath: String? = null,
    var isThumbnail: Boolean? = null
)
