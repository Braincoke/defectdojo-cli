package cli

import defectdojo.api.v1.*

/**
 * This class defines a set of useful functions to wrap the API
 */
class TerminalFormatter(val api: DefectDojoAPI) {
    companion object {
        /**
         * Format a matrix of information as a command line output.
         * The first row of the matrix should contain the headers.
         * The columns length will be adapted to the largest element of each column.
         */
        private fun asTable(stringMatrix: Array<Array<String>>): String {
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
            val padding = 5
            stringMatrix.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { index, string ->
                    table += String.format("%-${maxColumnLength[index] + padding}s", string)
                }
                table += "\n"
                if (rowIndex == 0) {
                    table += "-".repeat(maxColumnLength.sum() + maxColumnLength.size * padding) + "\n"
                }
            }
            return table
        }

        private val productHeader = arrayOf("ID", "NAME", "TYPE", "URI", "FINDINGS")
        /**
         * Format the response of a GET /products/ request
         */
        fun asTable(productsGetResponse: ProductsGetResponse?): String {
            if (productsGetResponse == null) return asTable(arrayOf(productHeader))
            val products = productsGetResponse.products
            return productsAsTable(products)
        }

        /**
         * Format the response of a GET /products/ request
         */
        fun productsAsTable(products: List<Product?>?): String {
            if (products.isNullOrEmpty() || products.filterNotNull().isEmpty()) return asTable(arrayOf(productHeader))
            val stringMatrix = Array(products.size + 1) { Array(productHeader.size) { "" } }
            stringMatrix[0] = productHeader
            products.forEachIndexed { rowIndex, product ->
                if (product != null)
                    stringMatrix[rowIndex + 1] = arrayOf(
                        product.id.toString(),
                        product.name ?: "#",
                        product.prodType ?: "#",
                        product.resourceUri ?: "#",
                        product.findingsCount.toString()
                    )
            }
            return asTable(stringMatrix)
        }


        private val productTypeHeader = arrayOf("ID", "NAME", "URI", "KEY_PRODUCT", "CRITICAL_PRODUCT")
        /**
         * Format the response of a GET /product_types/ request
         */
        fun asTable(productTypesGetResponse: ProductTypesGetResponse?): String {
            if (productTypesGetResponse == null) return asTable(arrayOf(productTypeHeader))
            val types = productTypesGetResponse.productTypes
            return productTypesAsTable(types)
        }

        /**
         * Format the response of a GET /product_types/{id} request
         */
        fun productTypesAsTable(productTypes: List<ProductType?>?): String {
            if (productTypes.isNullOrEmpty() || productTypes.filterNotNull().isEmpty()) return asTable(
                arrayOf(
                    productTypeHeader
                )
            )
            val stringMatrix = Array(productTypes.size + 1) { Array(productTypeHeader.size) { "" } }
            stringMatrix[0] = productTypeHeader
            productTypes.forEachIndexed { rowIndex, type ->
                if (type != null)
                    stringMatrix[rowIndex + 1] = arrayOf(
                        type.id.toString(),
                        type.name ?: "#",
                        type.resourceUri ?: "#",
                        type.keyProduct.toString(),
                        type.criticalProduct.toString()
                    )
            }
            return asTable(stringMatrix)
        }

        private val languageTypeHeader = arrayOf("ID", "URI", "LANGUAGE", "COLOR")
        /**
         * Format the response of a GET /language_types/ request
         */
        fun asTable(languageTypesGetResponse: LanguageTypesGetResponse?): String {
            if (languageTypesGetResponse == null) return asTable(arrayOf(languageTypeHeader))
            val types = languageTypesGetResponse.languageTypes
            return languageTypesAsTable(types)
        }

        /**
         * Format the response of a GET /language_types/{id} request
         */
        fun languageTypesAsTable(languageTypes: List<LanguageType?>?): String {
            if (languageTypes.isNullOrEmpty() || languageTypes.filterNotNull().isEmpty()) return asTable(
                arrayOf(languageTypeHeader)
            )
            val stringMatrix = Array(languageTypes.size + 1) { Array(languageTypeHeader.size) { "" } }
            stringMatrix[0] = languageTypeHeader
            languageTypes.forEachIndexed { rowIndex, type ->
                if (type != null)
                    stringMatrix[rowIndex + 1] = arrayOf(
                        type.id.toString(),
                        type.resourceUri ?: "#",
                        type.language ?: "#",
                        type.color.toString()
                    )
            }
            return asTable(stringMatrix)
        }


        private val languageHeader = arrayOf("ID", "LANGUAGE", "PRODUCT", "FILES", "CODE", "BLANK", "COMMENT", "URI")
        /**
         * Format the response of a GET /language_types/ request
         */
        fun asTable(languagesGetResponse: LanguagesGetResponse?): String {
            if (languagesGetResponse == null) return asTable(arrayOf(languageHeader))
            val languages = languagesGetResponse.languages
            return languagesAsTable(languages)
        }

        /**
         * Format the response of a GET /language_types/{id} request
         */
        fun languagesAsTable(languages: List<Language?>?): String {
            if (languages.isNullOrEmpty() || languages.filterNotNull().isEmpty()) return asTable(
                arrayOf(
                    languageHeader
                )
            )
            val stringMatrix = Array(languages.size + 1) { Array(languageHeader.size) { "" } }
            stringMatrix[0] = languageHeader
            languages.forEachIndexed { rowIndex, type ->
                if (type != null)
                    stringMatrix[rowIndex + 1] = arrayOf(
                        type.id.toString(),
                        type.languageType ?: "#",
                        type.product ?: "#",
                        type.files.toString(),
                        type.code.toString(),
                        type.blank.toString(),
                        type.comment.toString(),
                        type.resourceUri ?: "#"
                    )
            }
            return asTable(stringMatrix)
        }

        private val appAnalysisHeader = arrayOf("ID", "PRODUCT", "TECHNOLOGY", "URI")
        /**
         * Format the response of a GET /app_analysis/ request
         */
        fun asTable(appAnalysisGetResponse: AppAnalysisGetResponse?): String {
            if (appAnalysisGetResponse == null) return asTable(arrayOf(appAnalysisHeader))
            val technologies = appAnalysisGetResponse.appAnalysisList
            return appAnalysesAsTable(technologies)
        }

        /**
         * Format the response of a GET /app_analysis/{id} request
         */
        fun appAnalysesAsTable(technologies: List<AppAnalysis?>?): String {
            if (technologies.isNullOrEmpty() || technologies.filterNotNull().isEmpty()) return asTable(
                arrayOf(
                    appAnalysisHeader
                )
            )
            val stringMatrix = Array(technologies.size + 1) { Array(appAnalysisHeader.size) { "" } }
            stringMatrix[0] = appAnalysisHeader
            technologies.forEachIndexed { rowIndex, technology ->
                if (technology != null)
                    stringMatrix[rowIndex + 1] = arrayOf(
                        technology.id.toString(),
                        technology.product ?: "#",
                        technology.name ?: "#",
                        technology.resourceUri ?: "#"
                    )
            }
            return asTable(stringMatrix)
        }

        private val userHeader = arrayOf("ID", "USERNAME", "FIRST_NAME", "LAST_NAME", "URI", "LAST_LOGIN")
        /**
         * Format the response of a GET /language_types/ request
         */
        fun asTable(usersGetResponse: UsersGetResponse?): String {
            if (usersGetResponse == null) return asTable(arrayOf(userHeader))
            val users = usersGetResponse.users
            return usersAsTable(users)
        }

        /**
         * Format the response of a GET /language_types/{id} request
         */
        fun usersAsTable(users: List<User?>?): String {
            if (users.isNullOrEmpty() || users.filterNotNull().isEmpty()) return asTable(
                arrayOf(userHeader)
            )
            val stringMatrix = Array(users.size + 1) { Array(userHeader.size) { "" } }
            stringMatrix[0] = userHeader
            users.forEachIndexed { rowIndex, user ->
                if (user != null)
                    stringMatrix[rowIndex + 1] = arrayOf(
                        user.id.toString(),
                        user.username ?: "#",
                        user.firstName ?: "#",
                        user.lastName ?: "#",
                        user.resourceUri ?: "#",
                        user.lastLogin ?: "#"
                    )
            }
            return asTable(stringMatrix)
        }

    }
}