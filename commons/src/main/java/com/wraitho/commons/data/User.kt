package com.wraitho.commons.data

import com.squareup.moshi.Json


data class User(@Json(name = "login") val username: String,
                @Json(name = "id")val id: Long,
                @Json(name = "avatar_url") val avatarUrl: String)