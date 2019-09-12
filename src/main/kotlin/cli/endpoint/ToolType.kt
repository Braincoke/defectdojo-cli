package cli.endpoint

import cli.GetCommand
import cli.GetCommandWithName
import cli.PostCommand
import cli.getBody
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import defectdojo.api.v1.DefectDojoAPI
import defectdojo.api.v1.ToolType
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.provider
import retrofit2.Response


class ToolTypeCli : CliktCommand(
    name = "tool-type",
    help = """Manage tool types."""
) {
    override fun run() {}

}

/** Dependency injection module  **/
val toolTypeModule = Kodein.Module("tool-type") {
    bind<CliktCommand>().inSet() with provider {
        ToolTypeCli().subcommands(
            ToolTypeListCli(),
            ToolTypeIdCli(),
            ToolTypeAddCli(),
            ToolTypeUpdateCli(),
            ToolTypeDeleteCli()
        )
    }
}

class ToolTypeListCli : GetCommandWithName(
    name = "list",
    help = """Retrieve the list of tool types"""
) {

    override fun getFormattedResponse(dojoAPI: DefectDojoAPI): String {
        val response = dojoAPI.getToolTypes(
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

class ToolTypeIdCli : GetCommand(
    name = "id",
    help = """Retrieve a tool type from its identifier"""
) {

    private val id by argument(help = "The identifier of the tool type to retrieve").int()

    override fun getFormattedResponse(dojoAPI: DefectDojoAPI): String {
        val response = dojoAPI.getToolType(id)
            .execute()
        return tableFormatter.format(listOf(getBody(response)))
    }
}

class ToolTypeAddCli : PostCommand(
    name = "add",
    help = """Add a tool type."""
) {

    private val qName by argument("name", help = "The name of the tool type")
    private val qDescription by argument("description", help = "The description of the tool type")

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {
        val toolType = ToolType(
            name = qName,
            description = qDescription
        )
        return dojoAPI.addToolType(toolType).execute()
    }
}

class ToolTypeUpdateCli : PostCommand(
    name = "update",
    help = """Update a technology (app analysis) linked to a specified product.  
        |This action requires the auth|can change user permission.""".trimMargin()
) {

    private val qId by argument("id", help = "The identifier of the technology").int()
    private val qName by option("--name", help = "The name of the tool type")
    private val qDescription by option("--description", help = "The description of the tool type")

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {

        val toolType = ToolType(
            description = qDescription,
            name = qName
        )
        return dojoAPI.updateToolType(qId, toolType).execute()
    }
}

class ToolTypeDeleteCli : PostCommand(
    name = "delete",
    help = """Remove a tool type."""
) {

    private val qId by argument("id", help = "The identifier of the tool type").int()

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {
        return dojoAPI.deleteToolType(qId).execute()
    }
}