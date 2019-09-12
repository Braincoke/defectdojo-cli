package cli.endpoint

import cli.*
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import defectdojo.api.v1.DefectDojoAPI
import defectdojo.api.v1.ToolConfiguration
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.provider
import retrofit2.Response


class ToolConfigurationCli : CliktCommand(
    name = "tool-configuration",
    help = """Manage tool configurations."""
) {
    override fun run() {}

}

/** Dependency injection module  **/
val toolConfigurationModule = Kodein.Module("tool-configuration") {
    bind<CliktCommand>().inSet() with provider {
        ToolConfigurationCli().subcommands(
            ToolConfigurationListCli(),
            ToolConfigurationIdCli(),
            ToolConfigurationAddCli(),
            ToolConfigurationUpdateCli(),
            ToolConfigurationDeleteCli()
        )
    }
}

class ToolConfigurationListCli : GetCommandWithName(
    name = "list",
    help = """Retrieve the list of tool configurations"""
) {

    override fun getFormattedResponse(dojoAPI: DefectDojoAPI): String {
        val response = dojoAPI.getToolConfigurations(
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

class ToolConfigurationIdCli : GetCommand(
    name = "id",
    help = """Retrieve a tool configuration from its identifier"""
) {

    private val id by argument(help = "The identifier of the tool configuration to retrieve").int()

    override fun getFormattedResponse(dojoAPI: DefectDojoAPI): String {
        val response = dojoAPI.getToolConfiguration(id)
            .execute()
        return tableFormatter.format(listOf(getBody(response)))
    }
}

class ToolConfigurationAddCli : PostCommand(
    name = "add",
    help = """Add a tool configuration."""
) {

    private val qType by argument("type", help = "The type of the tool configuration").int()
    private val qName by argument("name", help = "The name of the tool configuration")
    private val qUrl by argument("url", help = "The url of the tool configuration")
    private val qDescription by option("--description", help = "The description of the tool configuration")
    private val qAuthType by option("--auth-type", help = "The authentication type of the tool configuration")
    private val qAuthTitle by option("--auth-title", help = "The authentication title of the tool configuration")
    private val qUsername by option("--username", help = "The username used for the authentication with the password")
    private val qPassword by option("--password", help = "The password used for the authentication with the username")
    private val qSSH by option("--ssh", help = "The ssh url used for the authentication")
    private val qApiKey by option("--api-key", help = "The api key used for the authentication")

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {
        val toolConfiguration = ToolConfiguration(
            toolType = getToolTypeUri(qType, dojoConfig),
            description = qDescription,
            name = qName,
            url = qUrl,
            authenticationType = qAuthType,
            authTitle = qAuthTitle,
            username = qUsername,
            password = qPassword,
            ssh = qSSH,
            apiKey = qApiKey
        )
        return dojoAPI.addToolConfiguration(toolConfiguration).execute()
    }
}
class ToolConfigurationUpdateCli : PostCommand(
    name = "update",
    help = """Update a tool configuration."""
) {

    private val qId by argument("id", help = "The identifier of the technology").int()
    private val qType by argument("type", help = "The type of the tool configuration").int()
    private val qName by option("--name", help = "The name of the tool configuration")
    private val qDescription by option("--description", help = "The description of the tool configuration")
    private val qUrl by option("--url", help = "The url of the tool configuration")
    private val qAuthType by option("--auth-type", help = "The authentication type of the tool configuration")
    private val qAuthTitle by option("--auth-title", help = "The title for the SSH or API key")
    private val qUsername by option("--username", help = "The username used for the authentication with the password")
    private val qPassword by option("--password", help = "The password used for the authentication with the username")
    private val qSSH by option("--ssh", help = "The ssh key used for the authentication")
    private val qApiKey by option("--api-key", help = "The api key used for the authentication")

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {
        val toolConfiguration = ToolConfiguration(
            toolType = getToolTypeUri(qType, dojoConfig),
            description = qDescription,
            name = qName,
            url = qUrl,
            authenticationType = qAuthType,
            authTitle = qAuthTitle,
            username = qUsername,
            password = qPassword,
            ssh = qSSH,
            apiKey = qApiKey
        )
        return dojoAPI.updateToolConfiguration(qId, toolConfiguration).execute()
    }
}

class ToolConfigurationDeleteCli : PostCommand(
    name = "delete",
    help = """Remove a tool configuration."""
) {

    private val qId by argument("id", help = "The identifier of the tool configuration").int()

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {
        return dojoAPI.deleteToolConfiguration(qId).execute()
    }
}