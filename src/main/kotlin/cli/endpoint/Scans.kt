package cli.endpoint

import cli.GetCommand
import cli.getBody
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import defectdojo.api.v1.DefectDojoAPI
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.provider

class ScanCli : CliktCommand(
    name = "scan",
    help = """Manage scans from the command line."""
) {
    override fun run() {}

}

/** Dependency injection module  **/
val scansModule = Kodein.Module("scan") {
    bind<CliktCommand>().inSet() with provider {
        ScanCli().subcommands(
            ScanListCli(),
            ScanIdCli()
        )
    }
}

class ScanListCli : GetCommand(
    name = "list",
    help = """Retrieve the list of scans"""
) {

    private val qLimit : String ?
            by option("--limit", help = "Specify the number of elements to retrieve per request")
    private val qOffset : String ?
            by option("--offset", help = "Specify the offset to start retrieving elements per request")

    private val  qSettingsContainsIgnoreCase by option("--settings-icontains",
        help = "Limit the research to scans whose settings contain this string ignoring casing")
    private val  qSettingsContains by option("--settings-contains",
        help = "Limit the research to scans whose settings contain this string with casing")
    private val  qSettings by option("--settings",
        help = "Limit the research to scans with the specified settings")


    override fun getFormattedResponse(dojoAPI: DefectDojoAPI): String {
        val response = dojoAPI.getScans(
            limit = qLimit,
            offset = qOffset,
            settings = qSettings,
            settingsContains = qSettingsContains,
            settingsContainsIgnoreCase = qSettingsContainsIgnoreCase
        )
            .execute()

        return tableFormatter.format(getBody(response))
    }
}

class ScanIdCli : GetCommand(
    name = "id",
    help = """Retrieve a scan from its identifier"""
) {
    private val id by argument(help = "The identifier of the scan to retrieve").int()

    override fun getFormattedResponse(dojoAPI: DefectDojoAPI): String {
        val response = dojoAPI.getScan(id)
            .execute()

        return tableFormatter.format(listOf(getBody(response)))
    }
}