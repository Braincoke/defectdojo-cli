package cli

import com.github.ajalt.clikt.core.PrintMessage
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.google.gson.JsonSyntaxException
import defectdojo.api.v1.DefectDojoAPI

abstract class GetCommand(help: String = "",
                           epilog: String = "",
                           name: String? = null,
                           invokeWithoutSubcommand: Boolean = false,
                           printHelpOnEmptyArgs: Boolean = false,
                           helpTags: Map<String, String> = emptyMap(),
                           autoCompleteEnvvar: String? = "")
: DojoCommand(help, epilog, name, invokeWithoutSubcommand, printHelpOnEmptyArgs, helpTags, autoCompleteEnvvar)  {

    companion object {
        const val ENVVAR_DISPLAY_FORMAT = "DEFECTDOJO_CLI_DISPLAY_FORMAT"
        const val ENVVAR_FULL_DISPLAY = "DEFECTDOJO_CLI_FULL_DISPLAY"
        const val ENVVAR_NO_HEADERS = "DEFECTDOJO_CLI_NO_HEADERS"
    }

    private val fullDisplay
            by option("--full", help = "Display every attribute of the retrieved object.",
                envvar = ENVVAR_FULL_DISPLAY).flag(default = false)
    private val noHeaders
            by option("--no-headers", help = "Do not display headers in the table formatting.",
                envvar = ENVVAR_NO_HEADERS).flag(default = false)
    private val format
            by option("--format", help = "Choose the format style.",
                envvar = ENVVAR_DISPLAY_FORMAT).choice(
                "console" to TableFormat.CONSOLE,
                "pretty" to TableFormat.PRETTY
            ).default(TableFormat.PRETTY)

    val tableFormatter by lazy {
        TableFormatter(noHeaders, fullDisplay, format)
    }

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
