package com.example.finalproject.ui.CheckIn

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.finalproject.R
import com.example.finalproject.ui.CheckIn.CheckInViewModel

class CheckInFragment : Fragment() {

    companion object {
        fun newInstance() = CheckInFragment()
    }

    private lateinit var viewModel: CheckInViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.check_in_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CheckInViewModel::class.java)
        // TODO: Use the ViewModel
    }

}