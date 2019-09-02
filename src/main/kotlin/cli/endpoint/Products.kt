package cli.endpoint

import DojoConfig
import cli.GetCommandWithOrder
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
    private val dojoConfig: DojoConfig by requireObject()

    override fun run() {
        val dojoAPI = dojoConfig.api
        try {
            val response = dojoAPI.getProducts(
                name = qName,
                limit = qLimit,
                offset = qOffset,
                nameContains = qNameContains,
                nameContainsIgnoreCase = qNameContainsIgnoreCase,
                orderBy = qOrderBy
            )
                .execute()
            val productsResponse = getBody(response)
            println(TerminalFormatter.asTable(productsResponse))
        } catch (e: JsonSyntaxException) {
            throw PrintMessage("Unexpected response from the DefectDojo server. Please check your connection information.")
        }
    }
}


class ProductsIdCli : CliktCommand(
    name = "id",
    help = """Retrieve a product from its id"""
) {
    private val dojoConfig: DojoConfig by requireObject()
    private val id by argument(help = "The identifier of the product").int()

    override fun run() {
        val dojoAPI = dojoConfig.api
        try {
            val response = dojoAPI.getProduct(id).execute()
            val product = getBody(response)
            println(TerminalFormatter.productsAsTable(listOf(product)))
        } catch (e: JsonSyntaxException) {
            throw PrintMessage("Unexpected response from the DefectDojo server. Please check your connection information.")
        }
    }
}