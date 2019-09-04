package cli.endpoint

import DojoConfig
import cli.TableFormatter
import cli.getBody
import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.PrintMessage
import com.github.ajalt.clikt.core.requireObject
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import com.google.gson.JsonSyntaxException
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.inSet
import org.kodein.di.generic.provider

class UserCli : CliktCommand(
    name = "user",
    help = """Manage users from the command line."""
) {
    override fun run() {}

}

/** Dependency injection module  **/
val usersModule = Kodein.Module("user") {
    bind<CliktCommand>().inSet() with provider {
        UserCli().subcommands(
            UserListCli(),
            UserIdCli()
        )
    }
}

class UserListCli : CliktCommand(
    name = "list",
    help = """Retrieve the list of users"""
) {
    private val dojoConfig: DojoConfig by requireObject()

    private val qLimit : String ?
            by option("--limit", help = "Specify the number of elements to retrieve per request")
    private val qOffset : String ?
            by option("--offset", help = "Specify the offset to start retrieving elements per request")

    private val  qUsernameContainsIgnoreCase by option("--username-icontains",
        help = "Limit the research to users whose username contain this string ignoring casing")
    private val  qUsernameContains by option("--username-contains",
        help = "Limit the research to users whose username contain this string with casing")
    private val  qUsername by option("--username",
        help = "Limit the research to users with the specified username")

    private val  qFirstNameContainsIgnoreCase by option("--firstname-icontains",
        help = "Limit the research to users whose first name contain this string ignoring casing")
    private val  qFirstNameContains by option("--firstname-contains",
        help = "Limit the research to users whose first name contain this string with casing")
    private val  qFirstName by option("--firstname",
        help = "Limit the research to users with the specified first name")

    private val  qLastNameContainsIgnoreCase by option("--lastname-icontains",
        help = "Limit the research to users whose last name contain this string ignoring casing")
    private val  qLastNameContains by option("--lastname-contains",
        help = "Limit the research to users whose last name contain this string with casing")
    private val  qLastName by option("--lastname",
        help = "Limit the research to users with the specified last name")
    override fun run() {
        val dojoAPI = dojoConfig.api
        try {
            val response = dojoAPI.getUsers(
                limit = qLimit,
                offset = qOffset,
                username = qUsername,
                usernameContains = qUsernameContains,
                usernameContainsIgnoreCase = qUsernameContainsIgnoreCase,
                firstName = qFirstName,
                firstNameContains = qFirstNameContains,
                firstNameContainsIgnoreCase = qFirstNameContainsIgnoreCase,
                lastName = qLastName,
                lastNameContains = qLastNameContains,
                lastNameContainsIgnoreCase = qLastNameContainsIgnoreCase
            )
                .execute()
            val users = getBody(response)
            println(TableFormatter.format(users))
        } catch (e: JsonSyntaxException) {
            throw PrintMessage("Unexpected response from the DefectDojo server. Please check your connection information.")
        }
    }
}

class UserIdCli : CliktCommand(
    name = "id",
    help = """Retrieve a user from its identifier"""
) {
    private val dojoConfig: DojoConfig by requireObject()
    private val id by argument(help = "The identifier of the user to retrieve").int()

    override fun run() {
        val dojoAPI = dojoConfig.api
        try {
            val response = dojoAPI.getUser(id)
                .execute()
            val user = getBody(response)
            println(TableFormatter.format(listOf(user)))
        } catch (e: JsonSyntaxException) {
            throw PrintMessage("Unexpected response from the DefectDojo server. Please check your connection information.")
        }
    }
}