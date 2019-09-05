package cli

import com.github.ajalt.clikt.core.PrintMessage
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