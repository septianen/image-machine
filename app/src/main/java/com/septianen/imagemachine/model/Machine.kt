package com.septianen.imagemachine.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.septianen.imagemachine.constant.Constant

@Entity(tableName = Constant.Table.MACHINE)
data class Machine(
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null,
    var name: String? = null,
    var type: String? = null,
    var qrNumber: Int? = null,
    var date: Long? = null,
    var thumbnail: String? = null

)
