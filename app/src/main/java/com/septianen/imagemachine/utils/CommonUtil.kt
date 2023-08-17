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

        fun convertToString(data: Int?): String? {
            return data?.toString()
        }

        fun convertToString(data: Long?): String? {
            return data?.toString()
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

        fun convertToLong(data: String?): Long? {
            return if (data.isNullOrEmpty())
                null
            else {
                try {
                    data.toLong()
                } catch (e: Exception) {
                    null
                }
            }
        }
    }
}