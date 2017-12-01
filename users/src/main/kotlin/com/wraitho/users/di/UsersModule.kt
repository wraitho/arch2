package com.wraitho.users.di


import com.wraitho.users.model.Model
import com.wraitho.users.model.NameFilter
import com.wraitho.users.model.UsersApi
import com.wraitho.users.service.UsersService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class UsersModule {

    @Provides
    fun providesUsersApi(retrofit: Retrofit): Model.UsersApi = UsersApi(retrofit.create(UsersService::class.java))

    @Provides
    fun providesNameFilter(): Model.NameFilter = NameFilter()

}