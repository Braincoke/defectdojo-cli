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
import defectdojo.api.v1.AppAnalysis
import defectdojo.api.v1.DefectDojoAPI
import defectdojo.api.v1.EndpointObject
import defectdojo.api.v1.EndpointResponseObject
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.provider
import retrofit2.Call
import retrofit2.Response
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
            AppAnalysisAddCli(),
            AppAnalysisDeleteCli()
        )
    }
}

class AppAnalysisListCli : GetCommandWithName(
    name = "list",
    help = """Retrieve the list of technologies (app analysis)"""
) {

    private val  qProductId by option("--product-id",
        help = "Limit the research to products with the specified identifier")
    private val  qProductNameContainsIgnoreCase by option("--product-name-icontains",
        help = "Limit the research to products whose name contain this string ignoring casing")
    private val  qProductName by option("--product-name",
        help = "Limit the research to products with the specified name")

    override fun getFormattedResponse(dojoAPI: DefectDojoAPI): String {
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
        return tableFormatter.format(getBody(response))
    }
}

class AppAnalysisIdCli : GetCommand(
    name = "id",
    help = """Retrieve a technology (app analysis) from its identifier"""
) {

    private val id by argument(help = "The identifier of the technology to retrieve").int()

    override fun getFormattedResponse(dojoAPI: DefectDojoAPI): String {
        val response = dojoAPI.getAppAnalysis(id)
            .execute()
        return tableFormatter.format(listOf(getBody(response)))
    }
}

class AppAnalysisAddCli : PostCommand(
    name = "add",
    help = """Add a technology (app analysis) to a specified product.  
        |This action requires the auth|can change user permission.""".trimMargin()
) {

    private val qName by argument("name", help = "The name of the technology")
    private val qId by argument("id", help = "The identifier of the product").int()
    private val qUser by argument("user", help = "The identifier of the user linked to the addition").int()
    private val qVersion by option("--version", help = "The version of the technology")
    private val qConfidence by option("--confidence", help = "The confidence of the app analysis").int()
    private val qWebsite by option("--website", help = "The website of the technology")

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {
        val appAnalysis = AppAnalysis(
            website = qWebsite,
            name = qName,
            user = "/api/${dojoConfig.apiVersion}/users/$qUser/",
            confidence = qConfidence,
            version = qVersion,
            product = "/api/${dojoConfig.apiVersion}/products/$qId/",
            created = LocalDateTime.now().toString()
        )
        return dojoAPI.addAppAnalysis(appAnalysis).execute()
    }
}


class AppAnalysisDeleteCli : PostCommand(
    name = "delete",
    help = """Remove a technology (app analysis) from a specified product.  
        |This action requires the auth|can change user permission.""".trimMargin()
) {

    private val qId by argument("id", help = "The identifier of the technology").int()

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {
        return dojoAPI.deleteAppAnalysis(qId).execute()
    }
}