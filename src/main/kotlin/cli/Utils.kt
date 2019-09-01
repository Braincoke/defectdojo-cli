package cli

import com.github.ajalt.clikt.core.PrintMessage
import retrofit2.Response


fun handleUnexpectedStatus(response: Response<*>) {
    when (response.code()) {
        200 -> {}
        401 -> throw PrintMessage("Unauthorized access to the API, " +
                "check your API key or your user permissions in the administration interface.")
        404 -> throw PrintMessage("Not found.")
        else -> throw PrintMessage("Unexpected error : ${response.message()}. You can debug with the --debug option.")
    }
}