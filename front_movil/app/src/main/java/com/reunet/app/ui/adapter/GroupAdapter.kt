package com.reunet.app.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        val itemGroupBinding = ItemGroupBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GroupViewHolder(itemGroupBinding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        val group: Group = listGroup[position]
        val context = holder.itemView.context

        holder.groupName.text = group.name

        holder.infoGroup.setOnClickListener{
            val intent = Intent(context, EditGroup::class.java)
            intent.putExtra("GROUP", group)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return listGroup.size
    }

    inner class GroupViewHolder(
        private val binding: ItemGroupBinding
    ): RecyclerView.ViewHolder(binding.root){
        val groupName:TextView = binding.groupName
        val infoGroup: ImageButton = binding.infoGroup
    }
}

