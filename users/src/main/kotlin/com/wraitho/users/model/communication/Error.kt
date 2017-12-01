package com.wraitho.users.model.communication

sealed class Error(val throwable: Throwable)
class NetworkError(throwable: Throwable) : Error(throwable)
class ServerError(throwable: Throwable) : Error(throwable)
class UnknownError(throwable: Throwable) : Error(throwable)