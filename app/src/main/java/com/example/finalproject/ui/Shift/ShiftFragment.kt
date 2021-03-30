package com.example.finalproject.ui.Shift

import Users
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.Utilities.GetAssetSource
import com.example.finalproject.Utilities.Utilities
import com.example.finalproject.ui.Schedule.shiftEmployeeAdapter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.shift_fragment.*
import java.net.URISyntaxException

class ShiftFragment : Fragment() {

    companion object {
        fun newInstance() = ShiftFragment()
    }

    var mSocket: Socket? = null
    private var url: String = ""
    private lateinit var adapter: shiftEmployeeAdapter
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

        return inflater.inflate(R.layout.shift_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        recyclerView = shiftEmployeeList
        mSocket?.on("setEmployee", onSetEmployeeList)
        mSocket?.emit("getEmployeeList")

        btnShiftNext.setOnClickListener {
            if (adapter.getSelectedList().size==0)
            {
                context?.let { it1 ->
                    Utilities().SimpleAlert("No items", "There are no items selected, select at least 1 to continue",
                        it1
                    )
                }
            }
             else{
                val bundle = bundleOf("selectedList" to adapter.getSelectedList())
                findNavController().navigate(R.id.shiftDetailsFragment, bundle)
            }
        }
    }

    var onSetEmployeeList = Emitter.Listener {
        val data = it[0] as String

        val moshi: Moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
        val jsonAdapter: JsonAdapter<List<Users>> = moshi.adapter(myType)

        val dataList = jsonAdapter.fromJson(data)

        adapter = dataList?.let { it1 -> shiftEmployeeAdapter(it1) }!!
        activity?.runOnUiThread(Runnable {
            //@Override
            this.run() {
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(activity)
            }
        })
    }

}