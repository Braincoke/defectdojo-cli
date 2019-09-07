package cli.endpoint

import cli.*
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.PrintMessage
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.clikt.parameters.types.int
import com.google.gson.JsonSyntaxException
import defectdojo.api.v1.DefectDojoAPI
import defectdojo.api.v1.ProductType
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.provider
import retrofit2.Response
import java.time.LocalDateTime

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
            ProductTypeIdCli(),
            ProductTypeAddCli(),
            ProductTypeUpdateCli()
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

        return tableFormatter.format(getBody(response))
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

        return tableFormatter.format(listOf(getBody(response)))
    }

    override fun run() {
        val dojoAPI = dojoConfig.api
        try {
            val response = dojoAPI.getProductType(id).execute()
            val productType = getBody(response)
            println(tableFormatter.format(listOf(productType)))
        } catch (e: JsonSyntaxException) {
            throw PrintMessage("Unexpected response from the DefectDojo server. Please check your connection information.")
        }
    }
}

class ProductTypeAddCli : PostCommand(
    name = "add",
    help = """Add a product type"""
) {
    private val qName by argument("name", help = "The name of the product type")
    private val qCriticalProduct by option(
        "--iCritical",
        help = "Indicate if the product is critical. Default is false."
    ).flag(default = false)
    private val qKeyProduct by option("--isKey", help = "Indicate if this is a key product. Default is false.").flag(
        default = false
    )

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {
        val productType = ProductType(
            name = qName,
            created = LocalDateTime.now().toString(),
            criticalProduct = qCriticalProduct,
            keyProduct = qKeyProduct,
            updated = LocalDateTime.now().toString()
        )
        return dojoAPI.addProductType(productType)
            .execute()
    }
}

class ProductTypeUpdateCli : PostCommand(
    name = "update",
    help = """Update a product type"""
) {
    private val qId by argument("id", help = "The identifier of the product type").int()
    private val qName by option("--name", help = "The name of the product type")
    private val qCriticalProduct
            by option("--critical", help = "Indicate if the product is critical.")
                .choice("true" to true, "false" to false)
    private val qKeyProduct
            by option("--key", help = "Indicate if this is a key product.")
                .choice("true" to true, "false" to false)

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {

        val productType = ProductType(
            id = qId,
            name = qName,
            criticalProduct = qCriticalProduct,
            keyProduct = qKeyProduct,
            updated = LocalDateTime.now().toString()
        )
        return dojoAPI.updateProductType(qId, productType)
            .execute()
    }
}