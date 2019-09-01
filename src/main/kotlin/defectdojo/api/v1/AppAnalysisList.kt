package defectdojo.api.v1

import com.google.gson.annotations.SerializedName

data class AppAnalysisList(

	@field:SerializedName("app_analysis")
	val appAnalysis: List<AppAnalysis?>? = null
)