package cli.endpoint

import DojoConfig
import cli.GetCommandWithOrder
import cli.handleUnexpectedStatus
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.PrintMessage
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.core.subcommands
import com.google.gson.JsonSyntaxException
import defectdojo.api.DefectDojoUtil
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.provider

class ProductTypesCli : CliktCommand(
    name = "product-type",
    help = """Manage product types from the command line."""
) {
    override fun run() {}
}

/** Dependency injection module  **/
val productTypeModule = Kodein.Module("product-type") {
    bind<CliktCommand>().inSet() with provider {
        ProductTypesCli().subcommands(
            ProductTypesListCli()
        )
    }
}

class ProductTypesListCli : GetCommandWithOrder(
    name = "list",
    help = """Retrieve the list of product types"""
) {
    private val dojoConfig: DojoConfig by requireObject()

    override fun run() {
        val dojoAPI = dojoConfig.api
        try {
            val response = dojoAPI.getProductTypes(
                name = qName,
                limit = qLimit,
                offset = qOffset,
                nameContains = qNameContains,
                nameContainsIgnoreCase = qNameContainsIgnoreCase,
                orderBy = qOrderBy
            )
                .execute()
            handleUnexpectedStatus(response)
            val productsResponse = response.body()
            println(DefectDojoUtil.formatAsTable(productsResponse))
        } catch (e: JsonSyntaxException) {
            throw PrintMessage("Unexpected response from the DefectDojo server. Please check your connection information.")
        }
    }
}