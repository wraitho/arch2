package com.wraitho.users.ui

import android.support.v7.widget.RecyclerView
import android.view.View
import com.squareup.picasso.Picasso
import com.wraitho.commons.data.User
import kotlinx.android.synthetic.main.list_item_users.view.userItemImage
import kotlinx.android.synthetic.main.list_item_users.view.userItemName

class UsersListItemView(itemView: View?, private val picasso: Picasso) : RecyclerView.ViewHolder(itemView) {
    fun bindView(user: User) {
        picasso.load(user.avatarUrl).into(itemView.userItemImage)
        itemView.userItemName.text = user.username
    }
}