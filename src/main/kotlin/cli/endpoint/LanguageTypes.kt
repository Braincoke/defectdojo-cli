package cli.endpoint

import cli.GetCommand
import cli.handleUnexpectedStatus
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.PrintMessage
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.int
import com.google.gson.JsonSyntaxException
import defectdojo.api.DefectDojoUtil
import defectdojo.api.v1.DefectDojoAPI
import defectdojo.api.v1.LanguageType
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.provider


class LanguageTypesCli : CliktCommand(
    name = "language-type",
    help = """Manage language types from the command line."""
) {
    val dojoAPI: DefectDojoAPI by requireObject()
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
    private val dojoAPI: DefectDojoAPI by requireObject()
    override fun run() {
        try {
            val response = dojoAPI.getLanguageTypes(
                name = qName,
                limit = qLimit,
                offset = qOffset,
                nameContains = qNameContains,
                nameContainsIgnoreCase = qNameContainsIgnoreCase
            )
                .execute()
            handleUnexpectedStatus(response)
            val languageTypes = response.body()
            println(DefectDojoUtil.formatAsTable(languageTypes))
        } catch (e: JsonSyntaxException) {
            throw PrintMessage("Unexpected response from the DefectDojo server. Please check your connection information.")
        }
    }
}

class LanguageTypesIdCli : CliktCommand(
    name = "id",
    help = """Retrieve a language type from its identifier"""
) {
    private val dojoAPI: DefectDojoAPI by requireObject()
    private val id by argument(help = "The identifier of the product type to retrieve").int()
    override fun run() {
        try {
            val response = dojoAPI.getLanguageType(id)
                .execute()
            handleUnexpectedStatus(response)
            val languageType = response.body()
            println(DefectDojoUtil.formatAsTable(listOf(languageType)))
        } catch (e: JsonSyntaxException) {
            throw PrintMessage("Unexpected response from the DefectDojo server. Please check your connection information.")
        }
    }
}