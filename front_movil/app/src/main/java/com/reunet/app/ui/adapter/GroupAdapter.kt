package com.reunet.app.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.R
import com.reunet.app.databinding.ItemGroupBinding
import com.reunet.app.models.Group
import com.reunet.app.ui.group.EditGroup

class GroupAdapter(
    private val listGroup: List<Group>,
): RecyclerView.Adapter<GroupAdapter.GroupViewHolder>(){
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GroupViewHolder {
        val itemGroupBinding = parent.inflate(R.layout.item_group)
        return GroupViewHolder(itemGroupBinding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group: Group = listGroup[position]
        val context = holder.itemView.context
        holder.bind(group, context)
    }

    override fun getItemCount(): Int {
        return listGroup.size
    }

    inner class GroupViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val binding = ItemGroupBinding.bind(view)
        fun bind(group: Group, context: Context){
                with(binding){
                    groupName.text = group.name
                    infoGroup.setOnClickListener{
                        val intent = Intent(context, EditGroup::class.java)
                        intent.putExtra("GROUP", group)
                        context.startActivity(intent)
                    }
                }
        }

    }
}

