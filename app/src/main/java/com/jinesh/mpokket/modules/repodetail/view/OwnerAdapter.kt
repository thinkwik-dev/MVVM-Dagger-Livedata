package com.jinesh.mpokket.modules.repodetail.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.jinesh.mpokket.R
import com.jinesh.mpokket.entity.Owner
import com.jinesh.mpokket.extension.glide
import kotlinx.android.synthetic.main.row_owner.view.*

class OwnerAdapter(var context: Context, var data: ArrayList<Owner>) :
    RecyclerView.Adapter<OwnerAdapter.ViewHolder>() {

    var click: (Owner) -> Unit = { }
    fun addItemClickListener(f: (Owner) -> Unit) {
        click = f
    }
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.row_owner,
                parent,
                false
            )
        )
    }
    override fun getItemCount(): Int = data.size
    inner class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Owner) {
            binding.setVariable(BR.data, data)
            binding.executePendingBindings()
            binding.root.ivAvtar.glide(context,data!!.avatar_url!!,20)
            binding.root.setOnClickListener { click(data) }
        }
    }
}

