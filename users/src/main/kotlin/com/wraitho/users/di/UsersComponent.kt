package com.wraitho.users.di

import com.wraitho.network.NetworkModule
import com.wraitho.users.ui.UsersFragment
import dagger.Component

@Component(modules = arrayOf(UsersModule::class, NetworkModule::class))
interface UsersComponent {
    fun inject(usersFragment: UsersFragment)
}