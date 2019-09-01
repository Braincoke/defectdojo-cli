package cli

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice

abstract class GetCommand(help: String = "",
                          epilog: String = "",
                          name: String? = null,
                          invokeWithoutSubcommand: Boolean = false,
                          printHelpOnEmptyArgs: Boolean = false,
                          helpTags: Map<String, String> = emptyMap(),
                          autoCompleteEnvvar: String? = "")
    : CliktCommand(help, epilog, name, invokeWithoutSubcommand, printHelpOnEmptyArgs, helpTags, autoCompleteEnvvar) {
    protected val qName : String?
            by option ("--name", help = "Limit the research to this exact name")
    protected val qNameContains : String ?
            by option("--name-contains", help = "Limit the research to products containing this string")
    protected val qNameContainsIgnoreCase : String ?
            by option("--iname-contains", help = "Limit the research to products containing this string ignoring casing")
    protected val qLimit : String ?
            by option("--limit", help = "Specify the number of elements to retrieve per request")
    protected val qOffset : String ?
            by option("--offset", help = "Specify the offset to start retrieving elements per request")
    protected val qOrderBy : String ?
            by option("--order", help = "Orders the result set based on the selection").choice(
                "name", "id", "description", "finding_count", "created", "product_type_id"
            )
}