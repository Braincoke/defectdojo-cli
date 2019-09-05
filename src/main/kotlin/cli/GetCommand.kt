package cli

import com.github.ajalt.clikt.core.PrintMessage
import com.google.gson.JsonSyntaxException
import defectdojo.api.v1.DefectDojoAPI

abstract class GetCommand(help: String = "",
                           epilog: String = "",
                           name: String? = null,
                           invokeWithoutSubcommand: Boolean = false,
                           printHelpOnEmptyArgs: Boolean = false,
                           helpTags: Map<String, String> = kotlin.collections.emptyMap(),
                           autoCompleteEnvvar: String? = "")
: DojoCommand(help, epilog, name, invokeWithoutSubcommand, printHelpOnEmptyArgs, helpTags, autoCompleteEnvvar)  {

    override fun run() {
        val dojoAPI = dojoConfig.api
        try {
            println(getFormattedResponse(dojoAPI))
        } catch (e: JsonSyntaxException) {
            throw PrintMessage("Unexpected response from the DefectDojo server. Please check your connection information.")
        }
    }

    abstract fun getFormattedResponse(dojoAPI: DefectDojoAPI) : String
}
