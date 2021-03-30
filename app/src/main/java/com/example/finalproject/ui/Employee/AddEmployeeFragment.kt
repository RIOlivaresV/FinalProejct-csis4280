package com.example.finalproject.ui.Employee

import android.app.Activity
import android.app.Activity.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.ParcelFileDescriptor.open
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.finalproject.MainActivity
import com.example.finalproject.R
import com.google.android.material.snackbar.Snackbar
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.fragment_add_employee.*
import org.json.JSONObject
import java.io.InputStream
import java.net.URISyntaxException
import java.nio.channels.AsynchronousFileChannel.open
import java.nio.channels.AsynchronousServerSocketChannel.open
import java.nio.channels.AsynchronousSocketChannel.open

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [AddEmployeeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddEmployeeFragment : Fragment() {
    // TODO: Rename and change types of parameters
    var mSocket: Socket? = null
    private var url: String = "http://100.25.40.176:5000/"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        var string: String?
        try {
            val inputStream: InputStream = context?.resources?.assets?.open("source.txt")!!
            val size: Int = inputStream.available()
            val buffer = ByteArray(size)
            inputStream.read(buffer)
            string = String(buffer)
            url = string
        } catch (e: Exception) {
            Log.d("error", e.message.toString())
        }

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

        mSocket?.on("notification", notificationSent)

    }

    var notificationSent = Emitter.Listener {
        val data =it[0] as Boolean
        if (data)
            activity?.runOnUiThread(Runnable {
                this.run {
                    findNavController().popBackStack()
                }
            })
        else
            Toast.makeText(context, "Something was wrong, try again", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_add_employee, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.save_employee_menu,menu)
//        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.saveEmployeeMenuItem->{
                val name = txtName.text.toString()
                val email = txtEmployeeEmail.text.toString()
                val phone = txtPhone.text.toString()
                val json: String = "{'name': '${name}', 'email':${email}, 'phone':${phone}}"
                val userJson = JSONObject(json)
                mSocket?.emit("newEmployee", userJson)
                hideKeyboard()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun hideKeyboard(){
        val inputMethodManager = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view?.windowToken, 0)
    }

}