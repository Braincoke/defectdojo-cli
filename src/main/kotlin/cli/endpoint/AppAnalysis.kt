package cli.endpoint

import cli.*
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import defectdojo.api.v1.AppAnalysis
import defectdojo.api.v1.DefectDojoAPI
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.provider
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
            AppAnalysisUpdateCli(),
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
            user = getUserUri(qUser, dojoConfig),
            confidence = qConfidence,
            version = qVersion,
            product = getProductUri(qId, dojoConfig),
            created = LocalDateTime.now().toString()
        )
        return dojoAPI.addAppAnalysis(appAnalysis).execute()
    }
}

class AppAnalysisUpdateCli : PostCommand(
    name = "update",
    help = """Update a technology (app analysis) linked to a specified product.  
        |This action requires the auth|can change user permission.""".trimMargin()
) {

    private val qId by argument("id", help = "The identifier of the technology").int()
    private val qName by option("--name", help = "The name of the technology")
    private val qProductId by option("--product", help = "The identifier of the product").int()
    private val qUser by option("--user", help = "The identifier of the user linked to the addition").int()
    private val qVersion by option("--version", help = "The version of the technology")
    private val qConfidence by option("--confidence", help = "The confidence of the app analysis").int()
    private val qWebsite by option("--website", help = "The website of the technology")

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {

        var productUri = qProductId?.let { getProductUri(it, dojoConfig) }
        var userUri = qUser?.let { getUserUri(it, dojoConfig) }

        if (qProductId == null || qUser == null) {
            val currentAppAnalysis = dojoAPI.getAppAnalysis(qId).execute().body()
            currentAppAnalysis?.let {
                if (qProductId == null) productUri = currentAppAnalysis.product ?: productUri
                if (qUser == null) userUri = currentAppAnalysis.user ?: userUri
            }
        }

        val appAnalysis = AppAnalysis(
            id = qId,
            website = qWebsite,
            name = qName,
            user = userUri,
            confidence = qConfidence,
            version = qVersion,
            product = productUri
        )
        return dojoAPI.updateAppAnalysis(qId, appAnalysis).execute()
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