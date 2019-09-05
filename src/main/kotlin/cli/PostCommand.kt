package cli

import com.github.ajalt.clikt.core.PrintMessage
import com.google.gson.JsonSyntaxException
import defectdojo.api.v1.DefectDojoAPI
import retrofit2.Response

abstract class PostCommand (help: String = "",
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
            val response = post(dojoAPI)
            when (response.code()) {
                201 -> println("Done")
                200 -> println("Done")
                401 -> println("Insufficient authorizations. Try --help for more information.")
                else -> println("An error occured. Try the option --debug to get more information.")
            }
        } catch (e: JsonSyntaxException) {
            throw PrintMessage("Unexpected response from the DefectDojo server. Please check your connection information.")
        }
    }

    abstract fun post(dojoAPI: DefectDojoAPI) : Response<Void>
}