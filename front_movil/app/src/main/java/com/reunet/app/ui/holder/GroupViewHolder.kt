package com.reunet.app.ui.holder

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.databinding.ItemGroupBinding
import com.reunet.app.models.Group
import com.reunet.app.models.Participant
import com.reunet.app.models.payload.request.ParticipantRequest
import com.reunet.app.services.ParticipantService
import com.reunet.app.ui.view.chat.storage.SharedPreferenceUtil
import com.reunet.app.ui.view.group.InfoGroupActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class GroupViewHolder(view: View): RecyclerView.ViewHolder(view){

    lateinit var  sharedPreferencesUtil: SharedPreferenceUtil

    private lateinit var participant: ParticipantRequest

    private var GROUP: String = "GROUP"

    private val participantService by lazy {
        sharedPreferencesUtil.getToken()?.let { ParticipantService.build(it) }
    }

    private val binding = ItemGroupBinding.bind(view)


    fun bind(group: Group, context: Context){

        with(binding){

            groupName.text = group.name

            sharedPreferencesUtil = SharedPreferenceUtil().also {
                it.setSharedPreference(context)
            }

            val idUser = sharedPreferencesUtil.getUser()?.id
            val idGroup = group.id

            if((sharedPreferencesUtil.getUser()?.id ?: String) != group.userId){
                infoGroup.visibility = View.GONE
                checkParticipantInGroup(group.id, context, group)
                joinGroup.setOnClickListener{
                    if(idUser != null) {
                        participant = ParticipantRequest(idGroup, idUser)
                    }
                    addParticipant(participant,context)
                    joinGroup.visibility = View.GONE
                }
            }else{
                infoGroup.setOnClickListener {
                    val intent = Intent(context, InfoGroupActivity::class.java)
                    intent.putExtra(GROUP, group)
                    context.startActivity(intent)
                }
            }
        }
    }

    private fun addParticipant(participant: ParticipantRequest, context: Context){
        CoroutineScope(Dispatchers.IO).launch {
            val call = participantService?.addParticipant(participant)
            try {
                if (call?.isSuccessful == true) {
                    Handler(Looper.getMainLooper()).post(Runnable { //It is queued to the main thread
                    fun run() {
                        Toast.makeText(context, "Successfully joined", Toast.LENGTH_SHORT).show()
                    }
                        run()
                    })
                }else{
                    throw Exception("credentials are invalid, CODE: ${call?.code()}")
                }
            } catch (e: Exception) {
                Log.i("Error adding participant", e.toString())
            }
        }
    }

    private fun checkParticipantInGroup(idGroup: String, context: Context, group: Group){

        CoroutineScope(Dispatchers.IO).launch {
            var inGroup = false
            val call = participantService?.getParticipantsByGroup(idGroup)
            if (call?.isSuccessful == true) {
                val participantResponse = call.body()
                val participants:List<Participant> = participantResponse?.data ?: emptyList()
                for (participant in participants){
                    if(participant.userId.toString() == sharedPreferencesUtil.getUser()?.id){
                        Handler(Looper.getMainLooper()).post(Runnable { //It is queued to the main thread
                        fun run() {
                            binding.joinGroup.visibility = View.GONE
                            binding.itemGroup.setOnClickListener {
                                val intent = Intent(context, InfoGroupActivity::class.java)
                                intent.putExtra(GROUP, group)
                                context.startActivity(intent)
                            }
                        }
                            run()
                        })
                        inGroup = true
                        break
                    }
                }

                if (!inGroup){
                    Handler(Looper.getMainLooper()).post(Runnable {
                        fun run() {
                            binding.joinGroup.visibility = View.VISIBLE
                        }
                        run()
                    })
                }
            }
        }
    }
}

