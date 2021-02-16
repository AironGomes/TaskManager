package com.airongomes.taskmanager.util

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.text.format.DateFormat
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

/**
 * This function is responsible to hide the keyboard
 */
fun hideKeyboard(view: View, context: Context) {
    val inputMethodManager = ContextCompat.getSystemService(context,
        InputMethodManager::class.java)!!
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * This function is responsible to show the TimePicker Dialog
 */
fun timePickerDialog(context: Context, cal: Calendar, timeSetListener: TimePickerDialog.OnTimeSetListener){
    val dialog = TimePickerDialog(context, timeSetListener,
        cal.get(Calendar.HOUR_OF_DAY),
        cal.get(Calendar.MINUTE), false)
    dialog.show()
}

/**
 * This function is responsible to show the DatePicker Dialog
 */
fun datePickerDialog(context: Context, cal: Calendar, dateSetListener: DatePickerDialog.OnDateSetListener){
    val dialog = DatePickerDialog(context, dateSetListener,
        cal.get(Calendar.YEAR),
        cal.get(Calendar.MONTH),
        cal.get(Calendar.DAY_OF_MONTH))
    dialog.show()
}

/**
 * This functions is used to format Date using current locale (FormatStyle.SHORT)
 */
fun formatDateStyleShort(cal: Calendar, context: Context): String{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
        val dateFormat = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))
        dateFormat.format(dateTimeFormatter)
    } else{
        val dateFormatter = DateFormat.getDateFormat(context)
        dateFormatter.format(cal.time)
    }
}

/**
 * This functions is used to format Date using current locale (FormatStyle.LONG)
 */
fun formatDateStyleLong(cal: Calendar, context: Context): String{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val dateTimeFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG)
        val dateFormat = LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH))
        dateFormat.format(dateTimeFormatter)
    } else{
        val dateFormatter = DateFormat.getLongDateFormat(context)
        dateFormatter.format(cal.time)
    }
}

/**
 * This functions is used to format Date&Time using current locale (FormatStyle.LONG)
 */
fun formatDateTimeStyleLong(cal: Calendar, context: Context): String{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG, FormatStyle.SHORT)
        val dateFormat = LocalDateTime.of(
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH) + 1,
                cal.get(Calendar.DAY_OF_MONTH),
                cal.get(Calendar.HOUR_OF_DAY),
                cal.get(Calendar.MINUTE))
        dateFormat.format(dateTimeFormatter)
    } else{
        val dateFormatter = DateFormat.getLongDateFormat(context)
        val timeFormatter = DateFormat.getTimeFormat(context)
        "${dateFormatter.format(cal.time)} ${timeFormatter.format(cal.time)}"
    }
}

/**
 * This functions is used to format Time using current locale
 */
fun formatTime(cal: Calendar, context: Context): String{
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val dateTimeFormatter = DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT)
        val timeFormat = LocalTime.of(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE))
        timeFormat.format(dateTimeFormatter)
    } else{
        val timeFormatter = DateFormat.getTimeFormat(context)
        timeFormatter.format(cal.time)
    }
}
