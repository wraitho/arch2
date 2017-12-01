package com.wraitho.users.model.communication

sealed class ResultStatus
class ResultSuccess : ResultStatus()
class ResultError : ResultStatus()
class ResultLoading : ResultStatus()

// todo remove 'Result' preposition if redeclaration is resolved