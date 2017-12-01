package com.wraitho.users.model.communication

data class Result<out T>(val status: ResultStatus, val data: T?, val error: Error? = null) {
    companion object {
        @JvmStatic fun <T> success(data: T): Result<T> =
                Result(ResultSuccess(), data, null)

        @JvmStatic @JvmOverloads fun <T> error(error: Error? = null, data: T? = null): Result<T> =
                Result(ResultError(), data, error)

        @JvmStatic @JvmOverloads fun <T> loading(data: T? = null): Result<T> =
                Result(ResultLoading(), data, null)
    }
}