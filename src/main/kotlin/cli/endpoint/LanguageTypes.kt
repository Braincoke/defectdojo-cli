package cli.endpoint

import cli.GetCommand
import cli.GetCommandWithName
import cli.PostCommand
import cli.getBody
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.PrintMessage
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.google.gson.JsonSyntaxException
import defectdojo.api.v1.DefectDojoAPI
import defectdojo.api.v1.LanguageType
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.provider
import retrofit2.Response


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
            LanguageTypesIdCli(),
            LanguageTypeAddCli(),
            LanguageTypeUpdateCli(),
            LanguageTypeDeleteCli()
        )
    }
}

class LanguageTypesListCli : GetCommandWithName(
    name = "list",
    help = """Retrieve the list of language types"""
) {

    override fun getFormattedResponse(dojoAPI: DefectDojoAPI): String {
        val response = dojoAPI.getLanguageTypes(
            name = qName,
            limit = qLimit,
            offset = qOffset,
            nameContains = qNameContains,
            nameContainsIgnoreCase = qNameContainsIgnoreCase
        )
            .execute()
        return tableFormatter.format(getBody(response))
    }
}

class LanguageTypesIdCli : GetCommand(
    name = "id",
    help = """Retrieve a language type from its identifier"""
) {
    private val id by argument(help = "The identifier of the product type to retrieve").int()


    override fun getFormattedResponse(dojoAPI: DefectDojoAPI): String {
        val response =  dojoAPI.getLanguageType(id)
            .execute()
        return tableFormatter.format(listOf(getBody(response)))
    }
    override fun run() {
        val dojoAPI = dojoConfig.api
        try {
            val response = dojoAPI.getLanguageType(id)
                .execute()
            val languageType = getBody(response)
            println(tableFormatter.format(listOf(languageType)))
        } catch (e: JsonSyntaxException) {
            throw PrintMessage("Unexpected response from the DefectDojo server. Please check your connection information.")
        }
    }
}


class LanguageTypeAddCli : PostCommand(
    name = "add",
    help = """Add a language type."""
) {

    private val qLanguage by argument("name", help = "The name of the language. Example : Kotlin, Python.")
    private val qColor by argument("color", help = "The color used to represent the language in charts in hex" +
            " format. Example : #F9EBEA.")

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {
        val languageType = LanguageType(
            language = qLanguage,
            color = qColor
        )
        return dojoAPI.addLanguageType(languageType).execute()
    }
}

class LanguageTypeUpdateCli : PostCommand(
    name = "update",
    help = """Update a language type."""
) {

    private val qId by argument("id", help = "The identifier of the language type").int()
    private val qName by option("--name", help = "The name of the language type")
    private val qColor by option("--color", help = "The color used to represent the language in charts in hex" +
            " format. Example : #F9EBEA.")

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {

        val languageType = LanguageType(
            id = qId,
            language = qName,
            color = qColor
        )
        return dojoAPI.updateLanguageType(qId, languageType).execute()
    }
}

class LanguageTypeDeleteCli : PostCommand(
    name = "delete",
    help = "Remove a language type."
) {

    private val qId by argument("id", help = "The identifier of the language type").int()

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {
        return dojoAPI.deleteLanguageType(qId).execute()
    }
}