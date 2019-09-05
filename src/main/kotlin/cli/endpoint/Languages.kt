package cli.endpoint

import DojoConfig
import cli.GetCommand
import cli.GetCommandWithName
import cli.TableFormatter
import cli.getBody
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.PrintMessage
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.google.gson.JsonSyntaxException
import defectdojo.api.v1.DefectDojoAPI
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

class LanguageListCli : GetCommandWithName(
    name = "list",
    help = """Retrieve the list of languages"""
) {

    private val  qProductId by option("--product-id",
        help = "Limit the research to products with the specified identifier")
    private val  qProductNameContainsIgnoreCase by option("--product-name-icontains",
        help = "Limit the research to products whose name contain this string ignoring casing")
    private val  qProductName by option("--product-name",
        help = "Limit the research to products with the specified name")

    override fun getFormattedResponse(dojoAPI: DefectDojoAPI): String {
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
        return TableFormatter.format(getBody(response))
    }
}

class LanguageIdCli : GetCommand(
    name = "id",
    help = """Retrieve a language from its identifier"""
) {

    private val id by argument(help = "The identifier of the language to retrieve").int()

    override fun getFormattedResponse(dojoAPI: DefectDojoAPI): String {
        val response = dojoAPI.getLanguage(id)
            .execute()
        return TableFormatter.format(listOf(getBody(response)))
    }
}