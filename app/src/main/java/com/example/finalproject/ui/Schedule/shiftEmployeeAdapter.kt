package com.example.finalproject.ui.Schedule

import Users
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.ui.Employee.employeesAdapter

class shiftEmployeeAdapter(private val employeeList: List<Users>) :
    RecyclerView.Adapter<shiftEmployeeAdapter.ViewHolder>() {

    private var selectedListener: MutableList<Users> = mutableListOf()

    inner class ViewHolder(itemView : View) :
        RecyclerView.ViewHolder(itemView){
        val txtEmployee : TextView = itemView.findViewById<TextView>(R.id.txtShiftEmployee)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.shift_employee_item, parent, false)
        return ViewHolder(view)
    }

    fun getSelectedList() : MutableList<Users>{
        return  selectedListener
    }

    override fun getItemCount(): Int {
        return  employeeList.size
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val employee= employeeList[position]
        holder.txtEmployee.text = employee.name
        holder.itemView.setOnClickListener(View.OnClickListener {
            selectedListener.add(employee)
            holder.itemView.isSelected = true
            holder.itemView.setBackgroundColor(Color.BLACK)
            holder.txtEmployee.setTextColor(Color.WHITE)
            Log.d("item", employee.name)
            Log.d("is selected?", holder.itemView.isSelected.toString())
            Log.d("Selected List size", selectedListener.size.toString())
        })
    }
}