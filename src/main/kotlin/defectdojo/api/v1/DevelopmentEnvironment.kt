package defectdojo.api.v1

import com.google.gson.annotations.SerializedName

data class DevelopmentEnvironment(
	@field:SerializedName("resource_uri")
	val resourceUri: String? = null,
	val name: String? = null,
	val id: Int? = null
) : DefectDojoObject
