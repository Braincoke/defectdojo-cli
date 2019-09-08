package cli

import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int

abstract class GetCommandWithName(help: String = "",
                                  epilog: String = "",
                                  name: String? = null,
                                  invokeWithoutSubcommand: Boolean = false,
                                  printHelpOnEmptyArgs: Boolean = false,
                                  helpTags: Map<String, String> = emptyMap(),
                                  autoCompleteEnvvar: String? = "")
    : GetCommand(help, epilog, name, invokeWithoutSubcommand, printHelpOnEmptyArgs, helpTags, autoCompleteEnvvar) {
    protected val qName : String?
            by option ("--name", help = "Limit the research to this exact name")
    protected val qNameContains : String ?
            by option("--name-contains", help = "Limit the research to products containing this string")
    protected val qNameContainsIgnoreCase : String ?
            by option("--iname-contains", help = "Limit the research to products containing this string ignoring casing")
    private val limit
            by option("--limit", help = "Specify the number of elements to retrieve per request").int()
    private val offset
            by option("--offset", help = "Specify the offset to start retrieving elements per request").int()
    protected  val qLimit : String ? by lazy {
        if (limit==null) { null } else { limit.toString() }
    }
    protected  val qOffset : String ? by lazy {
        if (offset==null) { null } else { offset.toString() }
    }
}