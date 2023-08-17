package com.septianen.imagemachine.utils

import java.lang.Exception

sealed class CommonUtil {

    companion object {
        fun convertToString(data: String?): String? {
            return if (data.isNullOrEmpty())
                null
            else {
                data
            }
        }

        fun convertToInt(data: String?): Int? {
            return if (data.isNullOrEmpty())
                null
            else {
                try {
                    data.toInt()
                } catch (e: Exception) {
                    null
                }
            }
        }
    }
}