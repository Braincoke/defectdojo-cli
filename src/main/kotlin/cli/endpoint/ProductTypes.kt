package cli.endpoint

import DojoConfig
import cli.GetCommand
import cli.GetCommandWithOrder
import cli.getBody
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.PrintMessage
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.types.int
import com.google.gson.JsonSyntaxException
import cli.TableFormatter
import defectdojo.api.v1.DefectDojoAPI
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
            ProductTypesListCli(),
            ProductTypeIdCli()
        )
    }
}

class ProductTypesListCli : GetCommandWithOrder(
    name = "list",
    help = """Retrieve the list of product types"""
) {
    override fun getFormattedResponse(dojoAPI: DefectDojoAPI): String {
        val response = dojoAPI.getProductTypes(
            name = qName,
            limit = qLimit,
            offset = qOffset,
            nameContains = qNameContains,
            nameContainsIgnoreCase = qNameContainsIgnoreCase,
            orderBy = qOrderBy
        )
            .execute()

        return TableFormatter.format(getBody(response))
    }
}

class ProductTypeIdCli : GetCommand(
    name = "id",
    help = """Retrieve a product type from its id"""
) {

    private val id by argument(help = "The identifier of the product").int()

    override fun getFormattedResponse(dojoAPI: DefectDojoAPI): String {
        val response = dojoAPI.getProductType(id)
            .execute()

        return TableFormatter.format(listOf(getBody(response)))
    }
    override fun run() {
        val dojoAPI = dojoConfig.api
        try {
            val response = dojoAPI.getProductType(id).execute()
            val productType = getBody(response)
            println(TableFormatter.format(listOf(productType)))
        } catch (e: JsonSyntaxException) {
            throw PrintMessage("Unexpected response from the DefectDojo server. Please check your connection information.")
        }
    }
}