package com.example.submissiondicodingintermediate_1.data.response

import com.google.gson.annotations.SerializedName

data class LoginResult (
    @field:SerializedName("userId")
    val userId: String?,

    @field:SerializedName("name")
    val name: String?,

    @field:SerializedName("token")
    val token: String?
)
