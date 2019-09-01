package defectdojo.api.v1


import com.google.gson.annotations.SerializedName

data class ProductTypesGetResponse(


	val meta: Meta? = null,
	@field:SerializedName("objects")
	val productTypes: List<ProductType>? = null
)