package com.wraitho.users.model

import com.wraitho.commons.data.User
import com.wraitho.users.service.UsersService
import io.reactivex.Observable

class UsersApi(private val userService: UsersService) : Model.UsersApi {
    override fun getUsers(lastLoadedId: Long): Observable<List<User>> {
        return userService.getUsers(lastLoadedId)
    }
}