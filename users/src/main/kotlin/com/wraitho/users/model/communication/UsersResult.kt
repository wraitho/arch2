package com.wraitho.users.model.communication

import com.wraitho.commons.data.User

data class UsersResult(val action: Action, val users: List<User>) {
    enum class Action {
        INITIAL_LOAD, APPEND, RELOAD, DO_NOTHING
    }
}
