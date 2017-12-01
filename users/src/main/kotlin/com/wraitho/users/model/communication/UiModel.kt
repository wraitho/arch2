package com.wraitho.users.model.communication

import com.wraitho.commons.data.User

sealed class UiModel
data class Success(val users: List<User>) : UiModel()
data class Failure(val error: Error) : UiModel()
class Loading : UiModel()
class Idle : UiModel()