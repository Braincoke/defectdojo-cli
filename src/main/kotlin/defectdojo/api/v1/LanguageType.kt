package defectdojo.api.v1

import com.google.gson.annotations.SerializedName

data class LanguageType(
	val color: String? = null,
	@field:SerializedName("resource_uri")
	val resourceUri: String? = null,
	val language: String? = null,
	val id: Int? = null
) : DefectDojoObject
