package defectdojo.api.v1

import com.google.gson.annotations.SerializedName

data class AppAnalysisGetResponse(

	val meta: Meta? = null,
	@field:SerializedName("objects")
	val appAnalysisList: AppAnalysisList? = null
)