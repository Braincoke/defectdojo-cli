package defectdojo.api.v1

import com.google.gson.annotations.SerializedName

data class ToolConfigurationsGetResponse(
	val meta: Meta? = null,
	@field:SerializedName("objects")
	val toolConfigurations: List<ToolConfiguration?>? = null
) : EndpointResponseObject
