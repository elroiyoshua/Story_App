package com.example.storybaru.responses

import com.google.gson.annotations.SerializedName

data class AddStoryResponses(

	@field:SerializedName("error")
	val error: Boolean,

	@field:SerializedName("message")
	val message: String
)
