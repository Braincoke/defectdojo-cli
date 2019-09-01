package defectdojo.api

import defectdojo.api.v1.DefectDojoAPI
import defectdojo.api.v1.ProductTypesGetResponse
import defectdojo.api.v1.ProductsGetResponse

/**
 * This class defines a set of useful functions to wrap the API
 */
class DefectDojoUtil(val api: DefectDojoAPI) {
    companion object {
        fun formatAsTable(stringMatrix: Array<Array<String>>): String {
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
    }
}