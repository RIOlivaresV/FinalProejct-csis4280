package com.example.finalproject.ui.Employee

import Users
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import org.json.JSONObject

class employeesAdapter(private val employeeList: List<Users>) :
    RecyclerView.Adapter<employeesAdapter.ViewHolder>() {

    inner class ViewHolder(itemView : View) :
        RecyclerView.ViewHolder(itemView){
        val txtEmployee : TextView = itemView.findViewById<TextView>(R.id.txtEemployeeName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.employee_element_layout, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return  employeeList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val employee= employeeList[position]
        holder.txtEmployee.text = employee.name
    }
}