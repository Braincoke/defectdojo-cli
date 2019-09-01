package cli

import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice

abstract class GetCommandWithOrder(help: String = "",
                          epilog: String = "",
                          name: String? = null,
                          invokeWithoutSubcommand: Boolean = false,
                          printHelpOnEmptyArgs: Boolean = false,
                          helpTags: Map<String, String> = emptyMap(),
                          autoCompleteEnvvar: String? = "")
    : GetCommand(help, epilog, name, invokeWithoutSubcommand, printHelpOnEmptyArgs, helpTags, autoCompleteEnvvar) {
    protected val qOrderBy : String ?
            by option("--order", help = "Orders the result set based on the selection").choice(
                "name", "id", "description", "finding_count", "created", "product_type_id"
            )
}