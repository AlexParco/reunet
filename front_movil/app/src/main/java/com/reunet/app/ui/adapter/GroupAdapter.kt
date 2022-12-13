package com.reunet.app.ui.adapter


import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.reunet.app.R
import com.reunet.app.models.Group
import com.reunet.app.ui.holder.GroupViewHolder

class GroupAdapter(
    private val listGroup: List<Group>
): RecyclerView.Adapter<GroupViewHolder>(){
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
}