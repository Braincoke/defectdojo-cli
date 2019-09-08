package defectdojo.api.v1

import com.google.gson.annotations.SerializedName

data class Language(
	val product: String? = null,
	val code: Int? = null,
	val blank: Int? = null,
	val created: String? = null,
	@field:SerializedName("resource_uri")
	val resourceUri: String? = null,
	val files: Int? = null,
	val comment: Int? = null,
	val id: Int? = null,
	@field:SerializedName("language_type")
	val languageType: String? = null,
	val user: String? = null
) : DefectDojoObject
