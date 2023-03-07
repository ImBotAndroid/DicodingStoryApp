package com.example.submissiondicodingintermediate_1.data.response

import com.google.gson.annotations.SerializedName

data class UploadStoriesResponse(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
