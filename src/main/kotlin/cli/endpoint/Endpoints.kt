package cli.endpoint

import cli.*
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import defectdojo.api.v1.Endpoint
import defectdojo.api.v1.DefectDojoAPI
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.provider
import retrofit2.Response
import java.net.URL


class EndpointCli : CliktCommand(
    name = "endpoint",
    help = """Manage endpoints."""
) {
    override fun run() {}

}

/** Dependency injection module  **/
val endpointModule = Kodein.Module("endpoint") {
    bind<CliktCommand>().inSet() with provider {
        EndpointCli().subcommands(
            EndpointListCli(),
            EndpointIdCli(),
            EndpointAddCli(),
            EndpointUpdateCli(),
            EndpointDeleteCli()
        )
    }
}

class EndpointListCli : GetCommand(
    name = "list",
    help = """Retrieve the list of endpoints"""
) {

    private val  qProductId by option("--product-id",
        help = "Limit the research to products with the specified identifier")
    private val  qProductNameContainsIgnoreCase by option("--product-name-icontains",
        help = "Limit the research to products whose name contain this string ignoring casing")
    private val  qProductName by option("--product-name",
        help = "Limit the research to products with the specified name")
    private val  qHostContainsIgnoreCase by option("--host-icontains",
        help = "Limit the research to products whose name contain this string ignoring casing")
    private val  qHost by option("--host",
        help = "Limit the research to products with the specified name")
    private val limit
            by option("--limit", help = "Specify the number of elements to retrieve per request").int()
    private val offset
            by option("--offset", help = "Specify the offset to start retrieving elements per request").int()
    private  val qLimit : String ? by lazy {
        if (limit==null) { null } else { limit.toString() }
    }
    private  val qOffset : String ? by lazy {
        if (offset==null) { null } else { offset.toString() }
    }

    override fun getFormattedResponse(dojoAPI: DefectDojoAPI): String {
        val response = dojoAPI.getEndpoints(
            limit = qLimit,
            offset = qOffset,
            productId = qProductId,
            productName = qProductName,
            productNameContainsIgnoreCase = qProductNameContainsIgnoreCase,
            host = qHost,
            hostContainsIgnoreCase = qHostContainsIgnoreCase
        )
            .execute()
        return tableFormatter.format(getBody(response))
    }
}

class EndpointIdCli : GetCommand(
    name = "id",
    help = """Retrieve a endpoint from its identifier"""
) {

    private val id by argument(help = "The identifier of the endpoint to retrieve").int()

    override fun getFormattedResponse(dojoAPI: DefectDojoAPI): String {
        val response = dojoAPI.getEndpoint(id)
            .execute()
        return tableFormatter.format(listOf(getBody(response)))
    }
}

class EndpointAddCli : PostCommand(
    name = "add",
    help = """Add a endpoint to a specified product."""
) {

    private val qProduct by argument("productId", help = "The identifier of the product.").int()
    private val qEndpoint by argument("endpoint", help = "The endpoint to add.")

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {
        val url = URL(qEndpoint)
        val fragments = qEndpoint.split("#")
        val fragment = if (fragments.size > 1) { fragments.last() } else { null }
        val endpoint = Endpoint(
            product = getProductUri(qProduct, dojoConfig),
            host = url.host,
            fragment = fragment,
            port = url.port,
            path = url.path,
            protocol = url.protocol,
            query = url.query
        )
        return dojoAPI.addEndpoint(endpoint).execute()
    }
}

class EndpointUpdateCli : PostCommand(
    name = "update",
    help = """Update a endpoint linked to a specified product."""
) {


    private val qId by argument("id", help = "The identifier of the endpoint to modify.").int()
    private val qProduct by option("--product", help = "The identifier of the endpoint's product.").int()
    private val qEndpoint by option("--endpoint", help = "The new endpoint.")

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {
        val endpointUrl =
            if (qEndpoint == null) {
                val currentEndpoint = dojoAPI.getEndpoint(qId).execute().body()
                currentEndpoint?.url
            } else {
                qEndpoint
            }

        val url = endpointUrl?.let { URL(it) }
        val endpoint = Endpoint(
            product =  qProduct?.let { getProductUri(it, dojoConfig)},
            host = url?.host,
            fragment = qEndpoint?.split("#")?.last(),
            port = url?.port,
            path = url?.path,
            protocol = url?.protocol,
            query = url?.query
        )
        return dojoAPI.updateEndpoint(qId, endpoint).execute()
    }
}

class EndpointDeleteCli : PostCommand(
    name = "delete",
    help = """Remove a endpoint from a specified product."""
) {

    private val qId by argument("id", help = "The identifier of the endpoint").int()

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {
        return dojoAPI.deleteEndpoint(qId).execute()
    }
}
