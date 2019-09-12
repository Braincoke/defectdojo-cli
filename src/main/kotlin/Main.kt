import cli.endpoint.*
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.PrintHelpMessage
import com.github.ajalt.clikt.core.UsageError
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import defectdojo.api.v1.DefectDojoAPI
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.setBinding
import java.util.logging.Logger

data class DojoConfig(
    var url: String,
    var username: String,
    var apiKey: String,
    var apiVersion: String,
    var debug: Boolean,
    var api : DefectDojoAPI
)

class Dojo : CliktCommand(
    name = "defectdojo-cli",
    help = """This command line tool allows you to use the DefectDojo tool fully from the 
        |comfort of your terminal.
        |
        |To connect to your running instance of DefectDojo yo can pass the --url, --username 
        |and --key arguments directly from the command line or by using
        |envinronment variables: DEFECTDOJO_CLI_URL, DEFECTDOJO_CLI_USERNAME and DEFECTDOJO_CLI_KEY.
    """.trimMargin()
) {
    companion object {
        const val ENVVAR_URL = "DEFECTDOJO_CLI_URL"
        const val ENVVAR_USERNAME = "DEFECTDOJO_CLI_USERNAME"
        const val ENVVAR_APIKEY = "DEFECTDOJO_CLI_KEY"
        const val DEFAULT_APIVERSION = "v1"
    }

    private val url: String? by option(
        help = "The base URL to the API. " +
                "Defaults to the environment variable $ENVVAR_URL.", envvar = ENVVAR_URL
    )
    private val username: String? by option(
        help = "Your DefectDojo username. " +
                "Defaults to the environment variable $ENVVAR_USERNAME.", envvar = ENVVAR_USERNAME
    )
    private val apiKey: String? by option(
        "--key", help = "The token to access the API. " +
                "Defaults to the environment variable $ENVVAR_APIKEY.", envvar = ENVVAR_APIKEY
    )
    private val apiVersion by option(
        "--apiversion", help = "The version of the API to use. " +
                "Defaults to $DEFAULT_APIVERSION."
    ).default(DEFAULT_APIVERSION)
    private val logger = Logger.getLogger(this::class.java.name)
    private val debug by option(help = "Print the http responses.").flag(default = false)


    override fun run() {
        if (url == null || apiKey == null || username == null) {
            val errorMessage = "Please specify the %s option or the %s environment variable"
            if (url == null)
                throw UsageError(String.format(errorMessage, "--url", ENVVAR_URL))
            if (apiKey == null)
                throw UsageError(String.format(errorMessage, "--key", ENVVAR_APIKEY))
            if (username == null)
                throw UsageError(String.format(errorMessage, "--username", ENVVAR_USERNAME))
            throw PrintHelpMessage(this)
        }
        val dojoAPI = DefectDojoAPI.create(url.toString(), username.toString(), apiKey.toString(), debug)

        val config = DojoConfig(
            url.toString(),
            username.toString(),
            apiKey.toString(),
            apiVersion,
            debug,
            dojoAPI)

        context.obj = config
    }
}


fun main(args: Array<String>) {

    val kodein = Kodein {
        bind() from setBinding<CliktCommand>()
        import(appAnalysisModule)
        import(developmentEnvironmentModule)
        import(languageModule)
        import(languageTypeModule)
        import(productModule)
        import(productTypeModule)
        import(usersModule)
        import(scansModule)
        import(toolTypeModule)
    }

    val commands: Set<CliktCommand> by kodein.instance()

    Dojo().subcommands(commands).main(args)
}