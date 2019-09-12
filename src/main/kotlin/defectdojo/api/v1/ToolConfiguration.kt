package defectdojo.api.v1

import com.google.gson.annotations.SerializedName

data class ToolConfiguration(
	val password: String? = null,
	@field:SerializedName("tool_type")
	val toolType: String? = null,
	@field:SerializedName("api_key")
	val apiKey: String? = null,
	@field:SerializedName("resource_uri")
	val resourceUri: String? = null,
	val name: String? = null,
	val description: String? = null,
	val ssh: String? = null,
	@field:SerializedName("auth_title")
	val authTitle: String? = null,
	val id: Int? = null,
	@field:SerializedName("authentication_type")
	val authenticationType: String? = null,
	val url: String? = null,
	val username: String? = null
) : DefectDojoObject
