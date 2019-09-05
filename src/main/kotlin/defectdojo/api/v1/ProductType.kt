package defectdojo.api.v1


import com.google.gson.annotations.SerializedName

data class ProductType(
	
	val created: String? = null,
	@field:SerializedName("resource_uri")
	val resourceUri: String? = null,
	val name: String? = null,
	@field:SerializedName("key_product")
	val keyProduct: Boolean? = null,
	val id: Int? = null,
	val updated: String? = null,
	@field:SerializedName("critical_product")
	val criticalProduct: Boolean? = null
) : DefectDojoObject