package defectdojo.api.v1

import com.google.gson.annotations.SerializedName

data class UsersGetResponse(
	val meta: Meta? = null,
	@field:SerializedName("objects")
	val users: List<User?>? = null
)
