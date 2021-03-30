package com.example.finalproject.ui.Employee

import Users
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.Utilities.GetAssetSource
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.employee_fragment.*
import kotlinx.android.synthetic.main.shift_fragment.*
import org.json.JSONObject
import java.io.InputStream
import java.net.URISyntaxException
import java.nio.channels.AsynchronousFileChannel.open

class EmployeeFragment : Fragment() {

    companion object {
        fun newInstance() = EmployeeFragment()
    }

    var mSocket: Socket? = null
    private var url: String = "http://100.25.40.176:5000/"
    private lateinit var viewModel: EmployeeViewModel
    private lateinit var adapter: employeesAdapter
    private lateinit var recyclerView: RecyclerView
    private val myType = Types.newParameterizedType(List::class.java, Users::class.java)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        url = context?.let { GetAssetSource().getAsset(it).toString() }.toString()
        try {
            mSocket = IO.socket(url)
        } catch (e: URISyntaxException) {
            Log.d("URI error", e.message.toString())
        }

        try {
            mSocket?.connect()
        } catch (e: Exception) {
            textViewResult.text = " Failed to connect. " + e.message
        }

        return inflater.inflate(R.layout.employee_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(EmployeeViewModel::class.java)
        btnAddEmployee.setOnClickListener {
            findNavController().navigate(R.id.addEmployeeFragment)
        }

        recyclerView = employeeList
        mSocket?.on("setEmployee", onSetEmployeeList)
        mSocket?.emit("getEmployeeList")
    }

//fill recycleview
    var onSetEmployeeList = Emitter.Listener {
        val data = it[0] as String

        val moshi: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter : JsonAdapter<List<Users>> = moshi.adapter(myType)

        val dataList = jsonAdapter.fromJson(data)

        adapter = dataList?.let { it1 -> employeesAdapter(it1) }!!
        activity?.runOnUiThread(Runnable {
            //@Override
            this.run() {
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(activity)
            }
        })



}

    override fun onDestroy() {
        super.onDestroy()
        mSocket?.disconnect()
    }

}