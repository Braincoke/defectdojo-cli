package cli.endpoint

import cli.GetCommandWithOrder
import cli.handleUnexpectedStatus
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.PrintMessage
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.core.subcommands
import com.google.gson.JsonSyntaxException
import defectdojo.api.DefectDojoUtil
import defectdojo.api.v1.DefectDojoAPI
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.provider

class ProductCli : CliktCommand(
    name = "product",
    help = """Manage products from the command line."""
) {
    val dojoAPI: DefectDojoAPI by requireObject()
    override fun run() {}
}

/** Dependency injection module  **/
val productModule = Kodein.Module("product") {
    bind<CliktCommand>().inSet() with provider {
        ProductCli().subcommands(
            ProductListCli()
        )
    }
}

class ProductListCli : GetCommandWithOrder(
    name = "list",
    help = """Retrieve the list of products"""
) {
    private val dojoAPI: DefectDojoAPI by requireObject()
    override fun run() {
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
            handleUnexpectedStatus(response)
            val productsResponse = response.body()
            println(DefectDojoUtil.formatAsTable(productsResponse))
        } catch (e: JsonSyntaxException) {
            throw PrintMessage("Unexpected response from the DefectDojo server. Please check your connection information.")
        }
    }
}