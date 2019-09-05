package defectdojo.api.v1

import com.google.gson.annotations.SerializedName

data class DevelopmentEnvironmentGetResponse(
	val meta: Meta? = null,
	@field:SerializedName("objects")
	val developmentEnvironments: List<DevelopmentEnvironment>? = null
) : EndpointResponseObject
