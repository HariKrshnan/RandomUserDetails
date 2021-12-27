package com.hkay.zohouserdetails.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.hkay.zohouserdetails.databinding.UserListItemBinding
import com.hkay.zohouserdetails.model.rickandmorty.Characters

class UserListAdapter(
    context: Context,
    private val listener: ((Characters?) -> Unit)? = null
) :
    ListAdapter<Characters, RecyclerView.ViewHolder>(UserDiffCallBack()) {
    var longClickListener: ((Characters?) -> Boolean)? = null
    val localContext = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding =
            UserListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserListViewHolder(
            binding
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.i("Length", position.toString())
        getItem(position)?.let { (holder as UserListViewHolder).bind(it) }
    }

    inner class UserListViewHolder(private val binding: UserListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: Characters) {
            Glide.with(localContext)
                .load(item.image)
                .fitCenter()
                .into(binding.itemImageView)
            binding.itemTextView.text = item.name
            binding.root.setOnClickListener {
                listener?.invoke(item)
            }
            binding.llImageView.setOnLongClickListener {
                longClickListener?.invoke(item)
                return@setOnLongClickListener true
            }
        }
    }

    class UserDiffCallBack : DiffUtil.ItemCallback<Characters>() {
        override fun areItemsTheSame(oldItem: Characters, newItem: Characters) = oldItem == newItem
        override fun areContentsTheSame(oldItem: Characters, newItem: Characters) =
            oldItem == newItem
    }
}