package com.example.finalproject.Utilities

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.util.Log

class Utilities {
    fun SimpleAlert(title: String, message: String, context: Context){
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setNegativeButton(android.R.string.cancel){ dialog, which ->  Log.d("Cancel alter", "Cancel Alert")}
    }
}