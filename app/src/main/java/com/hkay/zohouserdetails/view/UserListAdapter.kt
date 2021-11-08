package com.hkay.zohouserdetails.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.hkay.zohouserdetails.database.User
import com.hkay.zohouserdetails.databinding.UserListItemBinding

class UserListAdapter(context: Context, private val listener: ((User?) -> Unit)? = null) :
    androidx.recyclerview.widget.ListAdapter<User, RecyclerView.ViewHolder>(UserDiffCallBack()) {

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
        (holder as UserListViewHolder).bind(getItem(position))
    }

    inner class UserListViewHolder(private val binding: UserListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(item: User) {
            Glide.with(localContext)
                .load(item.picture)
                .into(binding.itemImageView)
            binding.itemTextView.text = item.name
            binding.root.setOnClickListener {
                listener?.invoke(item)
            }
        }
    }

    class UserDiffCallBack : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User) = oldItem == newItem
        override fun areContentsTheSame(oldItem: User, newItem: User) = oldItem == newItem
    }
}