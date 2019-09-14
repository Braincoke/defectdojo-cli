package defectdojo.api.v1

import com.google.gson.annotations.SerializedName

data class EndpointsGetResponse(
	val meta: Meta? = null,

	@field:SerializedName("objects")
	val endpoints: List<Endpoint?>? = null
) : EndpointResponseObject