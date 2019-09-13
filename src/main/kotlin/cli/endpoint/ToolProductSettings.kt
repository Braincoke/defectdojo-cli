package cli.endpoint

import cli.*
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import defectdojo.api.v1.ToolProductSetting
import defectdojo.api.v1.DefectDojoAPI
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.provider
import retrofit2.Response


class ToolProductSettingCli : CliktCommand(
    name = "tool-product-setting",
    help = """Manage tool product settings from the command line."""
) {
    override fun run() {}

}

/** Dependency injection module  **/
val toolProductSettingModule = Kodein.Module("tool-product-setting") {
    bind<CliktCommand>().inSet() with provider {
        ToolProductSettingCli().subcommands(
            ToolProductSettingListCli(),
            ToolProductSettingIdCli(),
            ToolProductSettingAddCli(),
            ToolProductSettingUpdateCli(),
            ToolProductSettingDeleteCli()
        )
    }
}

class ToolProductSettingListCli : GetCommandWithName(
    name = "list",
    help = """Retrieve the list of tool product settings """
) {

    override fun getFormattedResponse(dojoAPI: DefectDojoAPI): String {
        val response = dojoAPI.getToolProductSettings(
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

class ToolProductSettingIdCli : GetCommand(
    name = "id",
    help = """Retrieve a tool setting  from its identifier"""
) {

    private val id by argument(help = "The identifier of the tool setting to retrieve").int()

    override fun getFormattedResponse(dojoAPI: DefectDojoAPI): String {
        val response = dojoAPI.getToolProductSetting(id)
            .execute()
        return tableFormatter.format(listOf(getBody(response)))
    }
}

class ToolProductSettingAddCli : PostCommand(
    name = "add",
    help = """Add a tool setting  to a specified product.  
        |This action requires the auth|can change user permission.""".trimMargin()
) {

    private val qName by argument("name", help = "The name of the tool setting")
    private val qConfiguration by argument("configuration", help = "The identifier of the tool configuration").int()
    private val qDescription by option("--description", help = "The description of the tool product setting")
    private val qUrl by argument("url", help = "The URL to the tool")
    private val qToolProjectId : Int? by option("--tool-project-id", help = "The id of the project").int()

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {
        val toolProductSetting = ToolProductSetting(
            name = qName,
            url = qUrl,
            description = qDescription,
            toolConfiguration = getToolConfUri(qConfiguration, dojoConfig),
            toolProjectId = qToolProjectId?.let { getProductUri(it, dojoConfig) }
        )
        return dojoAPI.addToolProductSetting(toolProductSetting).execute()
    }
}

class ToolProductSettingUpdateCli : PostCommand(
    name = "update",
    help = """Update a tool setting  linked to a specified product.  
        |This action requires the auth|can change user permission.""".trimMargin()
) {

    private val qId by argument("id", help = "The identifier of the tool setting").int()
    private val qName by option("--name", help = "The name of the tool setting")
    private val qConfiguration by option("--configuration", help = "The identifier of the tool configuration").int()
    private val qDescription by option("--description", help = "The description of the tool product setting")
    private val qUrl by option("--url", help = "The URL to the tool")
    private val qToolProjectId : Int? by option("--tool-project-id", help = "The id of the project").int()

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {
        var url = qUrl
        var configuration = qConfiguration?.let { getToolConfUri(it, dojoConfig) }

        if (qUrl == null || qConfiguration == null) {
            val currentSetting = dojoAPI.getToolProductSetting(qId).execute().body()
            currentSetting?.let {
                if (qUrl == null) url = currentSetting.url ?: url
                if (qConfiguration == null) configuration = currentSetting.toolConfiguration ?: configuration
            }
        }

        val toolProductSetting = ToolProductSetting(
            name = qName,
            url = url,
            description = qDescription,
            toolConfiguration = configuration,
            toolProjectId = qToolProjectId?.let { getProductUri(it, dojoConfig) }
        )
        return dojoAPI.updateToolProductSetting(qId, toolProductSetting).execute()
    }
}

class ToolProductSettingDeleteCli : PostCommand(
    name = "delete",
    help = """Remove a tool setting."""
) {

    private val qId by argument("id", help = "The identifier of the tool setting").int()

    override fun post(dojoAPI: DefectDojoAPI): Response<Void> {
        return dojoAPI.deleteToolProductSetting(qId).execute()
    }
}