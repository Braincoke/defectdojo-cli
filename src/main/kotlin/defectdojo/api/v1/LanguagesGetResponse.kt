package defectdojo.api.v1

import com.google.gson.annotations.SerializedName

data class LanguagesGetResponse(
	val meta: Meta? = null,
	@field:SerializedName("objects")
	val languages: List<Language>? = null
) : EndpointResponseObject
