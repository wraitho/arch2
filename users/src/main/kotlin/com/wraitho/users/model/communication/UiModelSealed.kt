package com.wraitho.users.model.communication

import com.wraitho.commons.data.User

sealed class UiModelSealed
data class SuccessSed(val users: List<User>) : UiModelSealed()
data class FailureSed(val error: Error) : UiModelSealed()
class LoadingSed : UiModelSealed()
class IdleSed : UiModelSealed()