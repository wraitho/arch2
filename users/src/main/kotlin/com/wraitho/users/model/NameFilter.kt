package com.wraitho.users.model

import com.wraitho.commons.data.User
import io.reactivex.Observable

class NameFilter : Model.NameFilter{
    override fun filter(users: List<User>, name: String): Observable<List<User>> =
        Observable.just(users.filter { user -> user.username.toLowerCase().equals(name.toLowerCase()) })
}
