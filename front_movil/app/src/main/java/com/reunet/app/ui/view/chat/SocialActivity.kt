package com.reunet.app.ui.view.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.reunet.app.databinding.ActivitySocialBinding
import com.reunet.app.models.ChatGroup
import com.reunet.app.models.Message
import com.reunet.app.services.GroupService
import com.reunet.app.services.MessageService
import com.reunet.app.services.ParticipantService
import com.reunet.app.ui.view.chat.storage.SharedPreferenceUtil
import com.reunet.app.ui.adapter.SocialAdapter
import com.reunet.app.ui.view.BaseActivity
import com.reunet.app.ui.view.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SocialActivity : BaseActivity<ActivitySocialBinding>(ActivitySocialBinding::inflate) {

    //List Groups Chat
    private var chatGroupList = mutableListOf<ChatGroup>()

    private var auxListChatGroup = ArrayList<ChatGroup>()
    private lateinit var chatGroup: ChatGroup

    //Adapter
    private lateinit var socialAdapter: SocialAdapter

    //GET SERVICES
    private val groupService by lazy{
        sharedPreferencesUtil.getToken()?.let { GroupService.build(it) }
    }

    private val participantService by lazy{
        sharedPreferencesUtil.getToken()?.let { ParticipantService.build(it) }
    }

    private val messageService by lazy{
        sharedPreferencesUtil.getToken()?.let { MessageService.build(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesUtil = SharedPreferenceUtil().also {
            it.setSharedPreference(this)
        }

        getChatGroupItem()
        initRecyclerViewChatGroup()

        binding.toolbar2.setNavigationOnClickListener{
            finish()
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initRecyclerViewChatGroup(){
        socialAdapter = SocialAdapter(chatGroupList)
        binding.groupsList.layoutManager = LinearLayoutManager(this@SocialActivity)
        binding.groupsList.adapter = socialAdapter
    }

    private fun getChatGroupItem(){
        val user_id = sharedPreferencesUtil.getUser()?.id?.toInt()
        CoroutineScope(Dispatchers.IO).launch {
            val callGroup = groupService?.getGroups()
            try {
                 if (callGroup!!.isSuccessful){
                     val groupResponse = callGroup.body()?.data ?: emptyList()
                     for(group in groupResponse){
                         val idGroup = group.id
                         val callParticipant = participantService?.getParticipantsByGroup(idGroup)
                         val participantResponse = callParticipant!!.body()?.data ?: emptyList()
                         for(participant in participantResponse){
                             if(participant.userId == user_id){
                                 val callMessage = messageService?.getMessages(participant.groupId.toString())
                                 val messageResponse: List<Message>? = callMessage!!.body()?.data
                                 var message = "No messages yet"
                                 var date = ""
                                 if(messageResponse!!.isNotEmpty()){
                                     message = messageResponse.last().body
                                     date = messageResponse.last().createdAt.substring(0, 10)
                                 }
                                 chatGroup = ChatGroup(
                                     group,
                                     message,
                                     date
                                 )
                                 auxListChatGroup.add(chatGroup)
                                 runOnUiThread{
                                     chatGroupList.clear()
                                     chatGroupList.addAll(auxListChatGroup.toList())
                                     socialAdapter.notifyDataSetChanged()
                                 }
                             }
                         }
                     }
                 }else{
                     callGroup.errorBody()?.close()
                 }
            }catch (e: Exception){
                Log.i("Error with coroutines", e.toString())
            }
        }
    }
}