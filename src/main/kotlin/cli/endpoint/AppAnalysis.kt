package cli.endpoint

import DojoConfig
import cli.GetCommand
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
import defectdojo.api.v1.AppAnalysis
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.provider
import java.time.LocalDateTime


class AppAnalysisCli : CliktCommand(
    name = "app-analysis",
    help = """Manage technologies from the command line."""
) {
    override fun run() {}

}

/** Dependency injection module  **/
val appAnalysisModule = Kodein.Module("app-analysis") {
    bind<CliktCommand>().inSet() with provider {
        AppAnalysisCli().subcommands(
            AppAnalysisListCli(),
            AppAnalysisIdCli(),
            AppAnalysisAddCli()
        )
    }
}

class AppAnalysisListCli : GetCommand(
    name = "list",
    help = """Retrieve the list of technologies (app analysis)"""
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
            val response = dojoAPI.getAppAnalyses(
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
            val technologies = getBody(response)
            println(TableFormatter.format(technologies))
        } catch (e: JsonSyntaxException) {
            throw PrintMessage("Unexpected response from the DefectDojo server. Please check your connection information.")
        }
    }
}

class AppAnalysisIdCli : CliktCommand(
    name = "id",
    help = """Retrieve a technology (app analysis) from its identifier"""
) {
    private val dojoConfig: DojoConfig by requireObject()
    private val id by argument(help = "The identifier of the technology to retrieve").int()

    override fun run() {
        val dojoAPI = dojoConfig.api
        try {
            val response = dojoAPI.getAppAnalysis(id)
                .execute()
            val technology = getBody(response)
            println(TableFormatter.format(listOf(technology)))
        } catch (e: JsonSyntaxException) {
            throw PrintMessage("Unexpected response from the DefectDojo server. Please check your connection information.")
        }
    }
}

class AppAnalysisAddCli : CliktCommand(
    name = "add",
    help = """Add a technology (app analysis) to a specified product"""
) {
    private val dojoConfig: DojoConfig by requireObject()
    private val qName by argument("name", help = "The name of the technology")
    private val qId by argument("id", help = "The identifier of the product").int()
    private val qUser by argument("user", help = "The identifier of the user linked to the addition").int()
    private val qVersion by option("--version", help = "The version of the technology")
    private val qConfidence by option("--confidence", help = "The confidence of the app analysis").int()
    private val qWebsite by option("--website", help = "The website of the technology")

    override fun run() {
        val dojoAPI = dojoConfig.api
        val appAnalysis = AppAnalysis(
            website = qWebsite,
            name = qName,
            user = "/api/${dojoConfig.apiVersion}/users/$qUser/",
            confidence = qConfidence,
            version = qVersion,
            product = "/api/${dojoConfig.apiVersion}/products/$qId/",
            created = LocalDateTime.now().toString()
        )
        try {
            val response = dojoAPI.addAppAnalysis(appAnalysis)
                .execute()
            when (response.code()) {
                201 -> println("Technology added")
                200 -> println("Technology added")
                401 -> println("Insufficient authorizations. This action requires the auth|can change user permission.")
                else -> println("An error occured. Try the option --debug to get more information.")
            }
        } catch (e: JsonSyntaxException) {
            throw PrintMessage("Unexpected response from the DefectDojo server. Please check your connection information.")
        }
    }
}