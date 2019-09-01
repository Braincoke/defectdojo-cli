package defectdojo.api.v1

import com.google.gson.annotations.SerializedName

data class Product(

	@field:SerializedName("prod_type")
	val prodType: String? = null,
	@field:SerializedName("business_criticality")
	val businessCriticality: String? = null,
	val created: Any? = null,
	val origin: String? = null,
	@field:SerializedName("resource_uri")
	val resourceUri: String? = null,
	val description: String? = null,
	@field:SerializedName("internet_accessible")
	val internetAccessible: Boolean? = null,
	@field:SerializedName("findings_count")
	val findingsCount: Int? = null,
	val platform: String? = null,
	val lifecycle: String? = null,
	@field:SerializedName("prod_numeric_grade")
	val prodNumericGrade: Any? = null,
	val revenue: Any? = null,
	@field:SerializedName("external_audience")
	val externalAudience: Boolean? = null,
	val name: String? = null,
	@field:SerializedName("user_records")
	val userRecords: Any? = null,
	val id: Int? = null
)
