package cli

import DojoConfig
import com.github.ajalt.clikt.core.PrintMessage
import defectdojo.api.v1.DefectDojoAPI
import defectdojo.api.v1.EndpointObject
import retrofit2.Response


fun <T : EndpointObject> getBody(response: Response<T>) : T? {
    return when (response.code()) {
        200 -> response.body()
        401 -> throw PrintMessage("Unauthorized access to the API, " +
                "check your API key or your user permissions in the administration interface.")
        404 -> null
        else -> throw PrintMessage("Unexpected error : ${response.message()}. You can debug with the --debug option.")
    }
}


fun getLanguageTypeUri(languageTypeId: Int, dojoConfig: DojoConfig): String {
    return getObjectUri(languageTypeId, dojoConfig, DefectDojoAPI.LANGUAGE_TYPES)
}

fun getProductUri(productId: Int, dojoConfig: DojoConfig): String {
    return getObjectUri(productId, dojoConfig, DefectDojoAPI.PRODUCTS)
}
fun getToolConfUri(toolConfId: Int, dojoConfig: DojoConfig) : String {
    return getObjectUri(toolConfId, dojoConfig, DefectDojoAPI.TOOL_CONFIGURATIONS)
}

fun getToolTypeUri(toolTypeId: Int, dojoConfig: DojoConfig) : String {
    return getObjectUri(toolTypeId, dojoConfig, DefectDojoAPI.TOOL_TYPES)
}

fun getUserUri(userId: Int, dojoConfig: DojoConfig) : String {
    return getObjectUri(userId, dojoConfig, DefectDojoAPI.USERS)
}

fun getObjectUri(objectId: Int, dojoConfig: DojoConfig, endpoint: String) : String {
    return "/api/${dojoConfig.apiVersion}/$endpoint/$objectId/"
}


fun getPort(protocol: String?) : Int {
    if (protocol == null) return 80
    return when(protocol.toLowerCase()) {
        "http" -> 80
        "https" -> 443
        "ftp" -> 21
        "ssh" -> 22
        "smtp" -> 25
        "sftp" -> 989
        else -> 443
    }
}