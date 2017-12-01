package com.wraitho.users.service

import com.wraitho.commons.data.User
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface UsersService {

    @GET("users")
    fun getUsers(@Query("since") since: Long) : Observable<List<User>>
}