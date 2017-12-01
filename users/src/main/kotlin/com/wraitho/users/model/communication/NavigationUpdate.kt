package com.wraitho.users.model.communication

sealed class NavigationUpdate
data class GoToUserScreen(val id: Long) : NavigationUpdate()
class GoBack : NavigationUpdate()