package cli.endpoint

import DojoConfig
import cli.GetCommand
import cli.TerminalFormatter
import cli.getBody
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.PrintMessage
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.google.gson.JsonSyntaxException
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.provider


class LanguageCli : CliktCommand(
    name = "language",
    help = """Manage languages from the command line."""
) {
    override fun run() {}

}

/** Dependency injection module  **/
val languageModule = Kodein.Module("language") {
    bind<CliktCommand>().inSet() with provider {
        LanguageCli().subcommands(
            LanguageListCli(),
            LanguageIdCli()
        )
    }
}

class LanguageListCli : GetCommand(
    name = "list",
    help = """Retrieve the list of languages"""
) {
    private val dojoConfig: DojoConfig by requireObject()

    private val  qProductId by option("--product-id",
        help = "Limit the research to products with the specified identifier")
    private val  qProductNameContainsIgnoreCase by option("--product-name-icontains",
        help = "Limit the research to products whose name contain this string ignoring casing")
    private val  qProductName by option("--product-name",
        help = "Limit the research to products with the specified name")
    override fun run() {
        val dojoAPI = dojoConfig.api
        try {
            val response = dojoAPI.getLanguages(
                name = qName,
                limit = qLimit,
                offset = qOffset,
                nameContains = qNameContains,
                nameContainsIgnoreCase = qNameContainsIgnoreCase,
                productId = qProductId,
                productName = qProductName,
                productNameContainsIgnoreCase = qProductNameContainsIgnoreCase
            )
                .execute()
            val languages = getBody(response)
            println(TerminalFormatter.asTable(languages))
        } catch (e: JsonSyntaxException) {
            throw PrintMessage("Unexpected response from the DefectDojo server. Please check your connection information.")
        }
    }
}

class LanguageIdCli : CliktCommand(
    name = "id",
    help = """Retrieve a language from its identifier"""
) {
    private val dojoConfig: DojoConfig by requireObject()
    private val id by argument(help = "The identifier of the language to retrieve").int()

    override fun run() {
        val dojoAPI = dojoConfig.api
        try {
            val response = dojoAPI.getLanguage(id)
                .execute()
            val language = getBody(response)
            println(TerminalFormatter.languagesAsTable(listOf(language)))
        } catch (e: JsonSyntaxException) {
            throw PrintMessage("Unexpected response from the DefectDojo server. Please check your connection information.")
        }
    }
}