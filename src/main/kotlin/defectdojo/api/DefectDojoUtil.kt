package defectdojo.api

import defectdojo.api.v1.*

/**
 * This class defines a set of useful functions to wrap the API
 */
class DefectDojoUtil(val api: DefectDojoAPI) {
    companion object {
        /**
         * Format a matrix of information as a command line output.
         * The first row of the matrix should contain the headers.
         * The columns length will be adapted to the largest element of each column.
         */
        private fun formatAsTable(stringMatrix: Array<Array<String>>): String {
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
                    table += "-".repeat(maxColumnLength.sum() + maxColumnLength.size*padding) + "\n"
                }
            }
            return table
        }

        /**
         * Format the response of a GET /products/ request
         */
        fun formatAsTable(productsGetResponse: ProductsGetResponse? ) : String {
            val headerRow = arrayOf("ID", "NAME", "TYPE", "URI", "FINDINGS")
            if (productsGetResponse == null) return formatAsTable(arrayOf(headerRow))
            val products = productsGetResponse.products ?: return formatAsTable(arrayOf(headerRow))
            val stringMatrix = Array(products.size + 1) { Array(headerRow.size) { "" } }
            stringMatrix[0] = headerRow
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
            return formatAsTable(stringMatrix)
        }

        /**
         * Format the response of a GET /product_types/ request
         */
        fun formatAsTable(productTypesGetResponse: ProductTypesGetResponse? ) : String {
            val headerRow = arrayOf("ID", "NAME", "URI", "KEY_PRODUCT", "CRITICAL_PRODUCT")
            if (productTypesGetResponse == null) return formatAsTable(arrayOf(headerRow))
            val types = productTypesGetResponse.productTypes ?: return formatAsTable(arrayOf(headerRow))
            val stringMatrix = Array(types.size + 1) { Array(headerRow.size) { "" } }
            stringMatrix[0] = headerRow
            types.forEachIndexed { rowIndex, type ->
                if (type != null)
                    stringMatrix[rowIndex + 1] = arrayOf(
                        type.id.toString(),
                        type.name ?: "#",
                        type.resourceUri ?: "#",
                        type.keyProduct.toString(),
                        type.criticalProduct.toString()
                    )
            }
            return formatAsTable(stringMatrix)
        }

        private val languageTypeHeader = arrayOf("ID", "URI", "LANGUAGE", "COLOR")
        /**
         * Format the response of a GET /language_types/ request
         */
        fun formatAsTable(languageTypesGetResponse: LanguageTypesGetResponse? ) : String {
            if (languageTypesGetResponse == null) return formatAsTable(arrayOf(languageTypeHeader))
            val types = languageTypesGetResponse.languageTypes
            return formatAsTable(types)
        }

        /**
         * Format the response of a GET /language_types/{id} request
         */
        fun formatAsTable(languageTypes: List<LanguageType?>? ) : String {
            if (languageTypes == null) return formatAsTable(arrayOf(languageTypeHeader))
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
            return formatAsTable(stringMatrix)
        }
    }
}