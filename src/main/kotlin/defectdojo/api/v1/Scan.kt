package defectdojo.api.v1

import com.google.gson.annotations.SerializedName

data class Scan(
	val date: String? = null,
	val protocol: String? = null,
	@field:SerializedName("resource_uri")
	val resourceUri: String? = null,
	@field:SerializedName("scan_settings")
	val scanSettings: String? = null,
	val baseline: Boolean? = null,
	val id: Int? = null,
	val status: String? = null,
	val ipscans: String? = null
) : DefectDojoObject
