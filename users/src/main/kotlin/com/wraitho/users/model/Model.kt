package com.wraitho.users.model

import com.wraitho.commons.data.User
import io.reactivex.Observable

interface Model {

    interface UsersApi {
        fun getUsers(lastLoadedId: Long = 0): Observable<List<User>>
    }

    interface NameFilter {
        fun filter(users: List<User>, name: String) : Observable<List<User>>
    }

}