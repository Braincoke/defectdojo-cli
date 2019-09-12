package cli

import defectdojo.api.v1.*

/**
 * This class defines a set of useful functions to wrap the API
 */
class TableFormatter(private val noHeaders: Boolean, fullDisplay: Boolean, val format: TableFormat) {

    companion object {
        const val ERROR_MESSAGE = "An error occured. Try --debug for more information."
    }

    val appAnalysisHeaderMapping =
        if (fullDisplay) {
            mapOf(
                "ID" to AppAnalysis::id.name,
                "PRODUCT" to AppAnalysis::product.name,
                "TECHNOLOGY" to AppAnalysis::name.name,
                "URI" to AppAnalysis::resourceUri.name,
                "CONFIDENCE" to AppAnalysis::confidence.name,
                "WEBSITE" to AppAnalysis::website.name,
                "WEBSITE_FOUND" to AppAnalysis::websiteFound.name,
                "ICON" to AppAnalysis::icon.name,
                "CREATED" to AppAnalysis::created.name,
                "VERSION" to AppAnalysis::version.name,
                "USER" to AppAnalysis::user.name
            )
        } else {
            mapOf(
                "ID" to AppAnalysis::id.name,
                "PRODUCT" to AppAnalysis::product.name,
                "TECHNOLOGY" to AppAnalysis::name.name,
                "URI" to AppAnalysis::resourceUri.name
            )
        }

    val developmentEnvironmentHeaderMapping =
        mapOf(
            "ID" to DevelopmentEnvironment::id.name,
            "NAME" to DevelopmentEnvironment::name.name,
            "URI" to DevelopmentEnvironment::resourceUri.name
        )

    val languageHeaderMapping =
        if (fullDisplay) {
            mapOf(
                "ID" to Language::id.name,
                "LANGUAGE" to Language::languageType.name,
                "PRODUCT" to Language::product.name,
                "FILES" to Language::files.name,
                "CODE" to Language::code.name,
                "BLANK" to Language::blank.name,
                "COMMENT" to Language::comment.name,
                "URI" to Language::resourceUri.name,
                "CREATED" to Language::created.name,
                "USER" to Language::user.name
            )
        } else {
            mapOf(
                "ID" to Language::id.name,
                "LANGUAGE" to Language::languageType.name,
                "PRODUCT" to Language::product.name,
                "FILES" to Language::files.name,
                "CODE" to Language::code.name,
                "BLANK" to Language::blank.name,
                "COMMENT" to Language::comment.name,
                "URI" to Language::resourceUri.name
            )
        }


    val languageTypeHeaderMapping = mapOf(
        "ID" to LanguageType::id.name,
        "URI" to LanguageType::resourceUri.name,
        "LANGUAGE" to LanguageType::language.name,
        "COLOR" to LanguageType::color.name
    )
    val productHeaderMapping =
        if (fullDisplay) {
            mapOf(
                "ID" to Product::id.name,
                "NAME" to Product::name.name,
                "TYPE" to Product::prodType.name,
                "URI" to Product::resourceUri.name,
                "FINDINGS" to Product::findingsCount.name,
                "BUSINESS_CRITICALITY" to Product::businessCriticality.name,
                "INTERNET_ACCESSIBLE" to Product::internetAccessible.name,
                "EXTERNAL_AUDIENCE" to Product::externalAudience.name,
                "PLATFORM" to Product::platform.name,
                "LIFECYCLE" to Product::lifecycle.name,
                "REVENUE" to Product::revenue.name,
                "USER_RECORDS" to Product::userRecords.name,
                "PROD_NUMERIC_GRADE" to Product::prodNumericGrade.name,
                "ORIGIN" to Product::origin.name,
                "CREATED" to Product::created.name,
                "DESCRIPTION" to Product::description.name
            )
        } else {
            mapOf(
                "ID" to Product::id.name,
                "NAME" to Product::name.name,
                "TYPE" to Product::prodType.name,
                "URI" to Product::resourceUri.name,
                "FINDINGS" to Product::findingsCount.name
            )

        }

    val productTypeHeaderMapping =
        if (fullDisplay) {
            mapOf(
                "ID" to ProductType::id.name,
                "NAME" to ProductType::name.name,
                "URI" to ProductType::resourceUri.name,
                "KEY_PRODUCT" to ProductType::keyProduct.name,
                "CRITICAL_PRODUCT" to ProductType::criticalProduct.name,
                "CREATED" to ProductType::created.name,
                "UPDATED" to ProductType::updated.name
            )
        } else {
            mapOf(
                "ID" to ProductType::id.name,
                "NAME" to ProductType::name.name,
                "URI" to ProductType::resourceUri.name,
                "KEY_PRODUCT" to ProductType::keyProduct.name,
                "CRITICAL_PRODUCT" to ProductType::criticalProduct.name
            )
        }

    val userHeaderMapping: Map<String, String> =
        mapOf(
            "ID" to User::id.name,
            "USERNAME" to User::username.name,
            "FIRST_NAME" to User::firstName.name,
            "LAST_NAME" to User::lastName.name,
            "URI" to User::resourceUri.name,
            "LAST_LOGIN" to User::lastLogin.name)

    val scanHeaderMapping: Map<String, String> =
        mapOf(
            "ID" to Scan::id.name,
            "SETTINGS" to Scan::scanSettings.name,
            "STATUS" to Scan::status.name,
            "BASELINE" to Scan::baseline.name,
            "PROTOCOL" to Scan::protocol.name,
            "IPSCANS" to Scan::ipscans.name,
            "DATE" to Scan::date.name,
            "URI" to Scan::resourceUri.name
        )

    val toolTypesHeaderMapping: Map<String, String> =
        mapOf(
            "ID" to ToolType::id.name,
            "NAME" to ToolType::name.name,
            "DESCRIPTION" to ToolType::description.name,
            "URI" to ToolType::resourceUri.name
        )

    private fun format(stringMatrix: Array<Array<String>>): String {
        return if (format == TableFormat.PRETTY) {
            formatPretty(stringMatrix)
        } else {
            formatConsole(stringMatrix)
        }
    }

    /**
     * Format a matrix of information as a command line output.
     * The first row of the matrix should contain the headers.
     * The columns length will be adapted to the largest element of each column.
     *
     * @param stringMatrix : The matrix of strings to display as a table
     */
    private fun formatPretty(stringMatrix: Array<Array<String>>): String {
        if (stringMatrix.isEmpty()) return ""
        // Determine each maximum column length
        val maxColumnLength = Array(stringMatrix[0].size) { 0 }
        stringMatrix.forEach { row ->
            row.forEachIndexed { index, string ->
                maxColumnLength[index] = maxOf(maxColumnLength[index], string.length)
            }
        }
        // Display the matrix as a table
        var table = ""
        val padding = 3
        stringMatrix.forEachIndexed { rowIndex, row ->
            row.forEachIndexed { index, string ->
                table += String.format("%-${maxColumnLength[index] + padding}s", string)
            }
            table += "\n"
            if (rowIndex == 0 && !noHeaders) {
                table += "-".repeat(maxColumnLength.sum() + maxColumnLength.size * padding) + "\n"
            }
        }
        return table
    }

    /**
     * Format a matrix of information as a command line output.
     * The first row of the matrix should contain the headers.
     * The columns length will be adapted to the largest element of each column.
     *
     * @param stringMatrix : The matrix of strings to display as a table
     */
    private fun formatConsole(stringMatrix: Array<Array<String>>): String {
        if (stringMatrix.isEmpty()) return ""
        // Determine each maximum column length
        val maxColumnLength = Array(stringMatrix[0].size) { 0 }
        stringMatrix.forEach { row ->
            row.forEachIndexed { index, string ->
                maxColumnLength[index] = maxOf(maxColumnLength[index], string.length)
            }
        }
        // Display the matrix as a table
        var table = ""
        stringMatrix.forEach { row ->
            row.forEach { string ->
                table += "$string "
            }
            table += "\n"
        }
        return table
    }

    /**
     * Display an endpoint response in the standard output
     */
    fun format(element: EndpointResponseObject?): String {
        if (element == null) {
            return when (element) {
                is AppAnalysisGetResponse -> format(arrayOf(appAnalysisHeaderMapping.keys.toTypedArray()))
                is DevelopmentEnvironmentGetResponse -> format(arrayOf(developmentEnvironmentHeaderMapping.keys.toTypedArray()))
                is LanguagesGetResponse -> format(arrayOf(languageHeaderMapping.keys.toTypedArray()))
                is LanguageTypesGetResponse -> format(arrayOf(languageTypeHeaderMapping.keys.toTypedArray()))
                is ProductsGetResponse -> format(arrayOf(productHeaderMapping.keys.toTypedArray()))
                is ProductTypesGetResponse -> format(arrayOf(productTypeHeaderMapping.keys.toTypedArray()))
                is UsersGetResponse -> format(arrayOf(userHeaderMapping.keys.toTypedArray()))
                is ScansGetResponse -> format(arrayOf(scanHeaderMapping.keys.toTypedArray()))
                is ToolTypesGetResponse -> format(arrayOf(toolTypesHeaderMapping.keys.toTypedArray()))
                else -> ERROR_MESSAGE
            }
        }
        return when (element) {
            is AppAnalysisGetResponse -> format(element.appAnalysisList)
            is DevelopmentEnvironmentGetResponse -> format(element.developmentEnvironments)
            is LanguagesGetResponse -> format(element.languages)
            is LanguageTypesGetResponse -> format(element.languageTypes)
            is ProductsGetResponse -> format(element.products)
            is ProductTypesGetResponse -> format(element.productTypes)
            is UsersGetResponse -> format(element.users)
            is ScansGetResponse -> format(element.scans)
            is ToolTypesGetResponse -> format(element.toolTypes)
            else -> ERROR_MESSAGE
        }
    }

    /**
     * Display a list of objects as a table in the standard output
     * @author braincoke
     * @param list : the list of objects to display as a table
     * @param headerToAttribute : the list of attributes to display and the corresponding headers
     * @return the string representation of the list as a table
     */
    fun <T : Any> format(list: List<T?>?, headerToAttribute: Map<String, String>): String {
        if (list.isNullOrEmpty() || list.filterNotNull().isEmpty()) return format(
            arrayOf(headerToAttribute.keys.toTypedArray())
        )
        val matrixRowCount = if(noHeaders) { list.size } else { list.size + 1 }
        val stringMatrix = Array(matrixRowCount) { Array(headerToAttribute.size) { "" } }
        if (!noHeaders) {
            stringMatrix[0] = headerToAttribute.keys.toTypedArray()
        }
        // For each element of the list we add the selected attribute to the results displayed
        list.forEachIndexed { rowIndex, element ->
            if (element != null) {
                val row = Array(headerToAttribute.keys.size) { index ->
                    val header = headerToAttribute.keys.toList()[index]
                    val attributeName = headerToAttribute[header]
                    var attributeValue = "#"
                    val kCallable =
                        element::class.members.firstOrNull { kCallable -> attributeName == kCallable.name }
                    // We use kotlin reflection to retrieve
                    if (kCallable != null) {
                        attributeValue = kCallable.call(element).toString()
                    }
                    attributeValue
                }
                var matrixRowIndex = if(noHeaders) { rowIndex } else { rowIndex + 1 }
                stringMatrix[matrixRowIndex] = row
            }
        }
        return format(stringMatrix)
    }

    /**
     * Infer the type of a list of endpoints objects and display it as a table in the standard output
     */
    inline fun <reified T : EndpointObject> format(list: List<T?>?): String {
        return when (T::class.java.name) {
            AppAnalysis::class.java.name -> format(list, appAnalysisHeaderMapping)
            DevelopmentEnvironment::class.java.name -> format(list, developmentEnvironmentHeaderMapping)
            Language::class.java.name -> format(list, languageHeaderMapping)
            LanguageType::class.java.name -> format(list, languageTypeHeaderMapping)
            Product::class.java.name -> format(list, productHeaderMapping)
            ProductType::class.java.name -> format(list, productTypeHeaderMapping)
            User::class.java.name -> format(list, userHeaderMapping)
            Scan::class.java.name -> format(list, scanHeaderMapping)
            ToolType::class.java.name -> format(list, toolTypesHeaderMapping)
            else -> ERROR_MESSAGE
        }
    }

}