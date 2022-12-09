package com.reunet.app.ui.holder

import com.reunet.app.ui.view.chat.storage.SharedPreferenceUtil

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.databinding.ItemParticipantBinding
import com.reunet.app.models.User

class ParticipantViewHolder(view: View): RecyclerView.ViewHolder(view){
    private val binding = ItemParticipantBinding.bind(view)
    lateinit var  sharedPreferencesUtil: SharedPreferenceUtil

    fun bind(user: User, context: Context){

        sharedPreferencesUtil = SharedPreferenceUtil().also {
            it.setSharedPreference(context)
        }

        with(binding){
            if((sharedPreferencesUtil.getUser()?.email ?: "") == user.email){
                integrantName.text = "You"
            }else {
                integrantName.text = user.firstname
            }
            integrantEmail.text = user.email
            integrant.setOnClickListener{

            }
        }
    }
}