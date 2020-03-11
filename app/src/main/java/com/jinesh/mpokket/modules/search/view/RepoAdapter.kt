package com.jinesh.mpokket.modules.search.view

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.recyclerview.widget.RecyclerView
import com.jinesh.mpokket.R
import com.jinesh.mpokket.entity.Repo

class RepoAdapter(var context: Context) :
    RecyclerView.Adapter<RepoAdapter.ViewHolder>() {

    var  data: ArrayList<Repo> = ArrayList()

    var click: (Repo) -> Unit = { }

    fun addItemClickListener(f: (Repo) -> Unit) {
        click = f
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.row_repo,
                parent,
                false
            )
        )
    }


    override fun getItemCount(): Int = data.size
    inner class ViewHolder(val binding: ViewDataBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Repo) {
            binding.setVariable(BR.data, data)
            binding.executePendingBindings()
            binding.root.setOnClickListener { click(data) }
        }
    }
}

