package com.septianen.imagemachine.utils

sealed class CommonUtil {

    companion object {
        fun convertToString(data: String?): String? {
            return if (data.isNullOrEmpty())
                null
            else {
                data
            }
        }
    }
}