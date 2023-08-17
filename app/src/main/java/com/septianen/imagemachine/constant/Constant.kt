package com.septianen.imagemachine.constant

import com.septianen.imagemachine.model.Machine

object Constant {

    const val MAXIMUM_IMAGE = 10

    object DB {
        const val MACHINE = "machine.db"
    }

    object Table {
        const val MACHINE = "machine"
        const val IMAGE = "image"
    }

    object IntentExtra {
        const val IMAGE_PATH = "PATH"
    }

    object Dialog {
        const val BACK = 1
        const val DELETE = 2
    }
}