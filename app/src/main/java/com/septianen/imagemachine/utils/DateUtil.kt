package com.septianen.imagemachine.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

sealed class DateUtil {

    companion object {
        fun getDate(date: Long?): String? {

            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            var result = ""

            try {
                result = dateFormat.format(date)
                return result
            } catch (e: Exception) {
                e.printStackTrace()
            }

            return result
        }
    }
}