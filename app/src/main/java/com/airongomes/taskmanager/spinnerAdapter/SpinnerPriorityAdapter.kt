package com.airongomes.taskmanager.spinnerAdapter

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.airongomes.taskmanager.R
import kotlinx.android.synthetic.main.custom_layout_priority.view.*

class SpinnerPriorityAdapter(context: Context, listPriority: List<PriorityModel>):
    ArrayAdapter<PriorityModel>(context, 0, listPriority) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return this.customView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return this.customView(position, convertView, parent)
    }

    private fun customView(position: Int, convertView: View?, parent: ViewGroup): View {

        val priorityModel = getItem(position)
        val view = convertView ?: LayoutInflater.from(context).inflate(
            R.layout.custom_layout_priority,
            parent, false)

        priorityModel?.let {
            view.spinnerIcon.setImageResource(priorityModel.image)
            view.spinnerText.text = priorityModel.text }

        return view
    }

}