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

class ProductCli : CliktCommand(
    name = "product",
    help = """Manage products from the command line."""
) {
    override fun run() {}
}

/** Dependency injection module  **/
val productModule = Kodein.Module("product") {
    bind<CliktCommand>().inSet() with provider {
        ProductCli().subcommands(
            ProductListCli(),
            ProductsIdCli()
        )
    }
}

class ProductListCli : GetCommandWithOrder(
    name = "list",
    help = """Retrieve the list of products"""
) {

    override fun getFormattedResponse(dojoAPI: DefectDojoAPI): String {
        val response = dojoAPI.getProducts(
            name = qName,
            limit = qLimit,
            offset = qOffset,
            nameContains = qNameContains,
            nameContainsIgnoreCase = qNameContainsIgnoreCase,
            orderBy = qOrderBy
        )
            .execute()
        return tableFormatter.format(getBody(response))
    }
}


class ProductsIdCli : GetCommand(
    name = "id",
    help = """Retrieve a product from its id"""
) {
    private val id by argument(help = "The identifier of the product").int()

    override fun getFormattedResponse(dojoAPI: DefectDojoAPI): String {
        val response = dojoAPI.getProduct(id)
            .execute()
        return tableFormatter.format(listOf(getBody(response)))
    }
}