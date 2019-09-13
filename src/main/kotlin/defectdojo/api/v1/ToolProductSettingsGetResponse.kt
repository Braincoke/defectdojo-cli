package defectdojo.api.v1

import com.google.gson.annotations.SerializedName

data class ToolProductSettingsGetResponse(
	val meta: Meta? = null,

	@field:SerializedName("objects")
	val toolProductSettings: List<ToolProductSetting?>? = null
) : EndpointResponseObject