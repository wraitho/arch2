package com.wraitho.users.ui

import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.wraitho.commons.data.User
import com.wraitho.users.R
import com.wraitho.users.model.communication.LoadMoreUsersEvent
import com.wraitho.users.model.communication.UiEvent
import com.wraitho.users.widget.LoadMoreRecyclerView
import io.reactivex.subjects.PublishSubject
import org.jetbrains.anko.layoutInflater

class UsersListAdapter(private val listener: PublishSubject<UiEvent>, private val picasso: Picasso) :
        LoadMoreRecyclerView<UsersListItemView, UsersListFooterView>() {

    var users: List<User> = emptyList()

    init {
        loadMoreListener.subscribe {
            if (!users.isEmpty()) {
                listener.onNext(LoadMoreUsersEvent(users[users.size - 1].id))
            }
        }
    }

    override fun onCreateFooterViewHolder(parent: ViewGroup?): UsersListFooterView {
        return UsersListFooterView(parent?.context?.layoutInflater?.inflate(R.layout.list_item_users_footer, parent, false))
    }

    override fun onCreateNormalViewHolder(parent: ViewGroup?): UsersListItemView {
        return UsersListItemView(parent?.context?.layoutInflater?.inflate(R.layout.list_item_users, parent, false), picasso)
    }

    override fun onBindNormalView(holder: UsersListItemView?, position: Int) { holder?.bindView(users[position]) }

    override fun getRealItemCount(): Int = users.size

}
