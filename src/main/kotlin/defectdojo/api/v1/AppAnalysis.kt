package defectdojo.api.v1

import com.google.gson.annotations.SerializedName

data class AppAnalysis(

	val website: String? = null,
	val product: String? = null,
	val created: String? = null,
	val confidence: Int? = null,
	@field:SerializedName("resource_uri")
	val resourceUri: String? = null,
	val name: String? = null,
	val icon: String? = null,
	@field:SerializedName("website_found")
	val websiteFound: String? = null,
	val id: Int? = null,
	val version: String? = null,
	val user: String? = null
)