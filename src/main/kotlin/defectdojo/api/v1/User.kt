package defectdojo.api.v1

import com.google.gson.annotations.SerializedName

data class User(
	@field:SerializedName("last_login")
	val lastLogin: String? = null,
	@field:SerializedName("resource_uri")
	val resourceUri: String? = null,
	@field:SerializedName("last_name")
	val lastName: String? = null,
	val id: Int? = null,
	@field:SerializedName("first_name")
	val firstName: String? = null,
	val username: String? = null
)
