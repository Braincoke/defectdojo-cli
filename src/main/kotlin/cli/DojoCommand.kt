package cli

import DojoConfig
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.requireObject

abstract class DojoCommand (help: String = "",
                            epilog: String = "",
                            name: String? = null,
                            invokeWithoutSubcommand: Boolean = false,
                            printHelpOnEmptyArgs: Boolean = false,
                            helpTags: Map<String, String> = kotlin.collections.emptyMap(),
                            autoCompleteEnvvar: String? = "")
    : CliktCommand(help, epilog, name, invokeWithoutSubcommand, printHelpOnEmptyArgs, helpTags, autoCompleteEnvvar) {
    protected val dojoConfig: DojoConfig by requireObject()
}