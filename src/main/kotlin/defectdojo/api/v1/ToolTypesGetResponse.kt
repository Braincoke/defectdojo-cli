package defectdojo.api.v1

import com.google.gson.annotations.SerializedName

data class ToolTypesGetResponse(
	val meta: Meta? = null,
	@field:SerializedName("objects")
	val toolTypes: List<ToolType?>? = null
) : EndpointResponseObject
