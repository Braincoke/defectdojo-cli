package defectdojo.api.v1

import com.google.gson.annotations.SerializedName
import defectdojo.api.DefectDojoUtil
import java.lang.StringBuilder

data class ProductsGetResponse(
	val meta: Meta? = null,
	@field:SerializedName("objects")
	val products: List<Product?>? = null
)