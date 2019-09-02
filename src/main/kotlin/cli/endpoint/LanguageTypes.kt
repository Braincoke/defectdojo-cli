package cli.endpoint

import DojoConfig
import cli.GetCommand
import cli.getBody
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.PrintMessage
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.int
import com.google.gson.JsonSyntaxException
import cli.TerminalFormatter
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.provider


class LanguageTypesCli : CliktCommand(
    name = "language-type",
    help = """Manage language types from the command line."""
) {
    override fun run() {}

}

/** Dependency injection module  **/
val languageTypeModule = Kodein.Module("language-type") {
    bind<CliktCommand>().inSet() with provider {
        LanguageTypesCli().subcommands(
            LanguageTypesListCli(),
            LanguageTypesIdCli()
        )
    }
}

class LanguageTypesListCli : GetCommand(
    name = "list",
    help = """Retrieve the list of language types"""
) {
    private val dojoConfig: DojoConfig by requireObject()

    override fun run() {
        val dojoAPI = dojoConfig.api
        try {
            val response = dojoAPI.getLanguageTypes(
                name = qName,
                limit = qLimit,
                offset = qOffset,
                nameContains = qNameContains,
                nameContainsIgnoreCase = qNameContainsIgnoreCase
            )
                .execute()
            val languageTypes = getBody(response)
            println(TerminalFormatter.asTable(languageTypes))
        } catch (e: JsonSyntaxException) {
            throw PrintMessage("Unexpected response from the DefectDojo server. Please check your connection information.")
        }
    }
}

class LanguageTypesIdCli : CliktCommand(
    name = "id",
    help = """Retrieve a language type from its identifier"""
) {
    private val dojoConfig: DojoConfig by requireObject()
    private val id by argument(help = "The identifier of the product type to retrieve").int()

    override fun run() {
        val dojoAPI = dojoConfig.api
        try {
            val response = dojoAPI.getLanguageType(id)
                .execute()
            val languageType = getBody(response)
            println(TerminalFormatter.languageTypesAsTable(listOf(languageType)))
        } catch (e: JsonSyntaxException) {
            throw PrintMessage("Unexpected response from the DefectDojo server. Please check your connection information.")
        }
    }
}