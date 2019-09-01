package defectdojo.api.v1

import com.google.gson.annotations.SerializedName

data class LanguageTypesGetResponse(
	val meta: Meta? = null,
	@field:SerializedName("objects")
	val languageTypes: List<LanguageType?>? = null
)
