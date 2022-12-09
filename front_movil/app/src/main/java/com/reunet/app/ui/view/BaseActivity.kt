package com.reunet.app.ui.view

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.reunet.app.ui.view.chat.storage.SharedPreferenceUtil
import com.reunet.app.ui.fragment.DatePickerFragment

abstract class BaseActivity <B: ViewBinding>(val bindingFactory: (LayoutInflater) -> B): AppCompatActivity(){
    lateinit var binding: B

    lateinit var  sharedPreferencesUtil: SharedPreferenceUtil

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = bindingFactory(layoutInflater)
        setContentView(binding.root)
    }

    fun showDaterPickerDialog(btnDate:Button){
        val datePicker = DatePickerFragment{day, month, year ->onDateSelected(btnDate, day, month, year)}
        datePicker.show(supportFragmentManager, "datePicker")
    }

    private fun onDateSelected(btnDate:Button, day: Int, month: Int, year: Int){
        val dayP:String = if (day < 10) "0$day" else "$day"
        btnDate.text = "$year-${month+1}-$dayP"
    }
}
