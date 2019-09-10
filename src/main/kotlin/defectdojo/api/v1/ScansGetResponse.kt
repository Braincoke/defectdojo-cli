package defectdojo.api.v1

import com.google.gson.annotations.SerializedName

data class ScansGetResponse(
	@field:SerializedName("objects")
	val scans: List<Scan?>? = null,
	val meta: Meta? = null
) : EndpointResponseObject
