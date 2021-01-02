package com.airongomes.listadetarefas

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import java.util.Calendar

class SetCalendar {

    fun timePickerDialog(context: Context, cal: Calendar, timeSetListener: TimePickerDialog.OnTimeSetListener){
        val dialog = TimePickerDialog(context, timeSetListener,
            cal.get(Calendar.HOUR_OF_DAY),
            cal.get(Calendar.MINUTE), false)
        dialog.show()
    }

    fun datePickerDialog(context: Context, cal: Calendar, dateSetListener: DatePickerDialog.OnDateSetListener){
        val dialog = DatePickerDialog(context, dateSetListener,
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH))
        dialog.show()
    }
}