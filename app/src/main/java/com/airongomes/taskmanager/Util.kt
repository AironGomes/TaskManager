package com.airongomes.taskmanager

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat
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
