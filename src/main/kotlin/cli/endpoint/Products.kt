package cli.endpoint

import cli.*
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.types.int
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.choice
import defectdojo.api.v1.DefectDojoAPI
import defectdojo.api.v1.Product
import defectdojo.api.v1.ProductType
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.provider
import retrofit2.Response
import java.time.LocalDateTime

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
            ProductsIdCli(),
            ProductAddCli(),
            ProductUpdateCli()
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


class ProductAddCli : PostCommand(
    name = "add",
    help = """Add a product"""
) {
    private val qName by argument("name", help = "The name of the product")
    private val qType by argument("type", help = "The id of the product type").int()
    private val qDescription by argument("description", help = "The description of the product")
    private val qBusinessCriticality by option(
        "--criticality", help = "Indicate the criticality of the product."
    ).choice(
        "none" to "none",
        "very-low" to "low",
        "low" to "low",
        "medium" to "medium",
        "high" to "high",
        "very-high" to "very high"
    )
    private val qPlatform by option(
        "--platform", help = "Indicate the product platform."
    ).choice(
        "api" to "API",
        "desktop" to "Desktop",
        "iot" to "Internet of Things",
        "mobile" to "Mobile",
        "web" to "Web"
    )
    private val qOrigin by option(
        "--origin", help = "Indicate the product's origin."
    ).choice(
        "library" to "third party library",
        "purchased" to "purchased",
        "contractor" to "contractor",
        "internal" to "internal",
        "oss" to "open source",
        "outsourced" to "outsourced"
    )
    private val qLifecycle by option(
        "--lifecycle", help = "Indicate the product's lifecycle."
    ).choice(
        "construction" to "construction",
        "production" to "production",
        "retirement" to "retirement"
    )
    private val qExternal by option(
        "--external",
        help = "Indicate if the product is accessible by an external audience. Default is false."
    ).flag(default = false)
    private val qInternet by option(
        "--internet",
        help = "Indicate if the product is accessible from the internet. Default is false."
    ).flag(default = false)
    private val qRevenue by option(
        "--revenue",
        help = "Estimation of the application's revenue.")
    private val qUsers by option(
        "--users",
        help = "Estimation of the number of user records.")
    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {
        val product = Product(
            name = qName,
            created = LocalDateTime.now().toString(),
            description = qDescription,
            businessCriticality = qBusinessCriticality,
            externalAudience = qExternal,
            internetAccessible = qInternet,
            lifecycle = qLifecycle,
            origin = qOrigin,
            prodType = qType.toString(),
            revenue = qRevenue,
            platform = qPlatform,
            userRecords = qUsers
        )
        return dojoAPI.addProduct(product)
            .execute()
    }
}

class ProductUpdateCli : PostCommand(
    name = "update",
    help = """Update a product"""
) {
    private val qId by argument("id", help = "The identifier of the product").int()
    private val qDescription by option("--description", help = "The description of the product")
    private val qName by option("--name", help = "The name of the product")
    private val qType by option("--type", help = "The id of the product type").int()
    private val qBusinessCriticality by option(
        "--criticality", help = "Indicate the criticality of the product."
    ).choice(
        "none" to "none",
        "very-low" to "very low",
        "low" to "low",
        "medium" to "medium",
        "high" to "high",
        "very-high" to "very high"
    )
    private val qPlatform by option(
        "--platform", help = "Indicate the product platform."
    ).choice(
        "api" to "web service",
        "desktop" to "desktop",
        "iot" to "iot",
        "mobile" to "mobile",
        "web" to "web"
    )
    private val qOrigin by option(
        "--origin", help = "Indicate the product's origin."
    ).choice(
        "library" to "third party library",
        "purchased" to "purchased",
        "contractor" to "contractor",
        "internal" to "internal",
        "oss" to "open source",
        "outsourced" to "outsourced"
    )
    private val qLifecycle by option(
        "--lifecycle", help = "Indicate the product's lifecycle."
    ).choice(
        "construction" to "construction",
        "production" to "production",
        "retirement" to "retirement"
    )
    private val qExternal by option(
        "--external",
        help = "Indicate if the product is accessible by an external audience. Default is false."
    ).choice("true" to true, "false" to false)
    private val qInternet by option(
        "--internet",
        help = "Indicate if the product is accessible from the internet. Default is false."
    ).choice("true" to true, "false" to false)
    private val qRevenue by option(
        "--revenue",
        help = "Estimation of the application's revenue.")
    private val qUsers by option(
        "--users",
        help = "Estimation of the number of user records.")
    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {
        val product = Product(
            id = qId,
            name = qName,
            created = LocalDateTime.now().toString(),
            description = qDescription,
            businessCriticality = qBusinessCriticality,
            externalAudience = qExternal,
            internetAccessible = qInternet,
            lifecycle = qLifecycle,
            origin = qOrigin,
            prodType = if (qType == null) { null } else { qType.toString() } ,
            revenue = qRevenue,
            platform = qPlatform,
            userRecords = qUsers
        )
        return dojoAPI.updateProduct(qId, product)
            .execute()
    }
}
