package defectdojo.api.v1

import com.google.gson.annotations.SerializedName

data class ToolProductSetting(

	val product: String? = null,

	@field:SerializedName("tool_project_id")
	val toolProjectId: String? = null,
	@field:SerializedName("resource_uri")
	val resourceUri: String? = null,
	val name: String? = null,
	val description: String? = null,
	val id: Int? = null,
	@field:SerializedName("tool_configuration")
	val toolConfiguration: String? = null,
	val url: String? = null
) : DefectDojoObject