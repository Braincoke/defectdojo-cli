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
import defectdojo.api.v1.DevelopmentEnvironment
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


class DevelopmentEnvironmentCli : CliktCommand(
    name = "environment",
    help = """Manage development environments from the command line."""
) {
    override fun run() {}

}

/** Dependency injection module  **/
val developmentEnvironmentModule = Kodein.Module("development-environment") {
    bind<CliktCommand>().inSet() with provider {
        DevelopmentEnvironmentCli().subcommands(
            DevelopmentEnvironmentListCli(),
            DevelopmentEnvironmentIdCli(),
            DevelopmentEnvironmentAddCli()
        )
    }
}

class DevelopmentEnvironmentListCli : GetCommandWithName(
    name = "list",
    help = """Retrieve the list of technologies (app analysis)"""
) {

    override fun getFormattedResponse(dojoAPI: DefectDojoAPI): String {
        val response = dojoAPI.getDevelopmentEnvironments(
            name = qName,
            limit = qLimit,
            offset = qOffset,
            nameContains = qNameContains,
            nameContainsIgnoreCase = qNameContainsIgnoreCase
        )
            .execute()
        return tableFormatter.format(getBody(response))
    }
}

class DevelopmentEnvironmentIdCli : GetCommand(
    name = "id",
    help = """Retrieve a technology (app analysis) from its identifier"""
) {

    private val id by argument(help = "The identifier of the technology to retrieve").int()

    override fun getFormattedResponse(dojoAPI: DefectDojoAPI): String {
        val response = dojoAPI.getDevelopmentEnvironment(id)
            .execute()
        return tableFormatter.format(listOf(getBody(response)))
    }
}

class DevelopmentEnvironmentAddCli : PostCommand(
    name = "add",
    help = """Add a development environment"""
) {
    private val qName by argument("name", help = "The name of the development environment")

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {
        val developmentEnvironment = DevelopmentEnvironment(
            name = qName
        )
        return dojoAPI.addDevelopmentEnvironment(developmentEnvironment)
            .execute()
    }

}