package defectdojo.api.v1

import cli.getPort
import com.google.gson.annotations.SerializedName
import java.net.URI

data class Endpoint(

	val remediated: Boolean? = null,
	val path: String? = null,
	val protocol: String? = null,
	val product: String? = null,
	val fragment: String? = null,
	val fqdn: String? = null,
	val port: Int? = null,
	val query: String? = null,
	@field:SerializedName("resource_uri")
	val resourceUri: String? = null,
	val host: String? = null,
	val id: Int? = null
) : DefectDojoObject {

	val url : String
		get() = URI(protocol, null, host, port ?: getPort(protocol), path, query, fragment).toURL().toString()
}