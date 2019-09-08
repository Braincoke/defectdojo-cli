package cli.endpoint

import DojoConfig
import cli.*
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.PrintMessage
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.google.gson.JsonSyntaxException
import defectdojo.api.v1.DefectDojoAPI
import defectdojo.api.v1.Language
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.provider
import retrofit2.Response
import java.time.LocalDateTime


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
            LanguageIdCli(),
            LanguageAddCli(),
            LanguageUpdateCli(),
            LanguageDeleteCli()
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
        return tableFormatter.format(getBody(response))
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
        return tableFormatter.format(listOf(getBody(response)))
    }
}


class LanguageAddCli : PostCommand(
    name = "add",
    help = """Add a language to a specified product."""
) {

    private val qType by argument("type", help = "The identifier of the type of the language.").int()
    private val qProduct by argument("product", help = "The identifier of the product where the language will be added.").int()
    private val qUser by argument("user", help = "The identifier of the user adding this language.").int()
    private val qCode by option("--code", help = "The number of lines of codes.").int()
    private val qComment by option("--comment", help = "The number of lines of comments.").int()
    private val qBlank by option("--blank", help = "The number of blank lines.").int()
    private val qFiles by option("--files", help = "The number of files.").int()

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {
        val language = Language(
            languageType =  getLanguageTypeUri(qType, dojoConfig),
            product = getProductUri(qProduct, dojoConfig),
            user = getUserUri(qUser, dojoConfig),
            created = LocalDateTime.now().toString(),
            code = qCode,
            comment = qComment,
            blank = qBlank,
            files = qFiles
        )
        return dojoAPI.addLanguage(language).execute()
    }
}

class LanguageUpdateCli : PostCommand(
    name = "update",
    help = """Update a language."""
) {

    private val qId by argument("id", help = "The identifier of the language").int()
    private val qProduct by argument("product", help = "The identifier of the product where the language will be updated.").int()
    private val qUser by argument("user", help = "The identifier of the user adding this language.").int()
    private val qType : Int? by option("--type", help = "The identifier of the type of the language.").int()
    private val qCode by option("--code", help = "The number of lines of codes.").int()
    private val qComment by option("--comment", help = "The number of lines of comments.").int()
    private val qBlank by option("--blank", help = "The number of blank lines.").int()
    private val qFiles by option("--files", help = "The number of files.").int()

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {

        val language = Language(
            languageType = qType?.let { getLanguageTypeUri(it, dojoConfig) },
            product =  getProductUri(qProduct, dojoConfig) ,
            user = getUserUri(qUser, dojoConfig),
            code = qCode,
            comment = qComment,
            blank = qBlank,
            files = qFiles
        )
        return dojoAPI.updateLanguage(qId, language).execute()
    }
}

class LanguageDeleteCli : PostCommand(
    name = "delete",
    help = "Remove a language."
) {

    private val qId by argument("id", help = "The identifier of the language.").int()

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {
        return dojoAPI.deleteLanguage(qId).execute()
    }
}