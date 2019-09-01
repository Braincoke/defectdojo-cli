package defectdojo.api.v1

data class Meta(

	val next: String? = null,
	val previous: String? = null,
	val offset: String? = null,
	val totalCount: String? = null,
	val limit: String? = null
)