package com.wraitho.users.model.communication

sealed class UiEvent
data class UserSelectedEvent(val userId: Long) : UiEvent()
data class LoadMoreUsersEvent(val lastLoadedId: Long = 0) : UiEvent()
data class NameSearchEvent(val name: String) : UiEvent()
class Back : UiEvent()