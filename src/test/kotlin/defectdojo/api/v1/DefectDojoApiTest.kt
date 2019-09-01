package defectdojo.api.v1

import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import retrofit2.Response
import java.net.ConnectException
import java.time.LocalDateTime
import java.util.logging.Logger
import javax.naming.ConfigurationException

class DefectDojoApiTest {

    private lateinit var defectDojo : DefectDojoAPI
    private lateinit var apiBaseUrl : String
    private lateinit var apiKey: String
    private lateinit var username : String


    companion object {
        const val PRODUCT_TYPE_NAME = "Data visualisation"
        const val PRODUCT_TYPE_NAME_UPDATE = "Big data management"
        const val PRODUCT_NAME = "DatEye"
        const val PRODUCT_NAME_UPDATE = "NewLook"
        val LOGGER : Logger = Logger.getLogger(DefectDojoApiTest::class.java.name)
    }

    @BeforeEach
    fun setup() {
        readConfig()
        checkConnection()
        defectDojo = DefectDojoAPI.create(apiBaseUrl, username, apiKey, debug = true)
    }

    /**
     * This functions reads the connection information from the configuration file defectdojo-config.ini
     */
    private fun readConfig() {
        val configFile = this::class.java.classLoader.getResource("defectdojo-config.ini")
        checkNotNull(configFile) { "Please specify a file defectdojo-config.ini in the test resources"}
        val config = mutableMapOf<String, String>()
        for (line in configFile.readText().lines()) {
            val elements = line.split("=")
            val key = elements[0]
            val value = elements.subList(1, elements.size).joinToString(separator = "")
            config[key] = value
        }
        val baseUrlKey = "baseUrl"
        val apiKeyKey = "apiKey"
        val usernameKey = "username"
        val urlErrorMessage = "The API base URL must be set in the configuration file"
        val apiErrorMessage = "The API key must be set in the configuration file"
        val usernameErrorMessage = "The username must be set in the configuration file"
        when {
            !config.containsKey(baseUrlKey) -> throw ConfigurationException(urlErrorMessage)
            !config.containsKey(apiKeyKey) -> throw ConfigurationException(apiErrorMessage)
            !config.containsKey(usernameKey) -> throw ConfigurationException(usernameErrorMessage)
        }
        checkNotNull(config[baseUrlKey])  { urlErrorMessage }
        checkNotNull(config[apiKeyKey]) { apiErrorMessage }
        checkNotNull(config[usernameKey]) { usernameErrorMessage }
        apiBaseUrl = config[baseUrlKey]!!
        apiKey = config[apiKeyKey]!!
        username = config[usernameKey]!!
    }

    /** Verify that a DefectDojo instance is running */
    private fun checkConnection() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(apiBaseUrl)
            .build()
        try {
            val response = client.newCall(request).execute()
            assert(response.isSuccessful || response.isRedirect)
            LOGGER.info("Connection to DefectDojo detected")
        } catch (exception: ConnectException) {
            LOGGER.severe("Connection to DefectDojo not detected. " +
                    "Please ensure you have a running instance and configure the connection parameters.")
        }
    }

    /************************************************************
     * Product API tests
     ************************************************************/

    /**
     * Add the product type "Data visualisation"
     */
    @Test
    fun addProductType() {
        val productType = ProductType(
            name = "Data visualisation",
            created = LocalDateTime.now().toString(),
            criticalProduct = false,
            keyProduct = false
        )
        val response = defectDojo.addProductType(productType).execute()
        LOGGER.info("ProductType successfully added")
        when {
            response.isSuccessful -> {
                LOGGER.info("ProductType successfully added")
            }
            else -> handleResponseError(response)
        }
    }

    /**
     * List all product types
     */
    @Test
    fun getProductTypes() {
        val response = defectDojo.getProductTypes().execute()
        when {
            response.isSuccessful -> {
                LOGGER.info("Products retrieved :")
                response.body()?.productTypes?.forEach {
                    LOGGER.info(it.name)
                }
            }
            else -> handleResponseError(response)
        }
    }

    /**
     * Retrieve one product by its exact name
     */
    @Test
    fun getProductTypeByName() {
        val response = defectDojo.getProductTypes(name = PRODUCT_TYPE_NAME).execute()
        when {
            response.isSuccessful -> {
                val types = response.body()?.productTypes
                assertNotNull(types)
                assert(types?.size == 1)
                assert(types?.get(0)?.name == PRODUCT_TYPE_NAME)
                LOGGER.info("Product Type ID ${types?.get(0)?.id}")
            }
            else -> handleResponseError(response)
        }
    }

    /**
     * Update a product. It must exist first.
     */
    @Test
    fun updateProductType() {
        // Verify that the product type exists
        val productTypesResponse = defectDojo.getProductTypes().execute().body()
        assertNotNull(productTypesResponse)
        assertNotNull(productTypesResponse?.productTypes)
        assertNotNull(productTypesResponse?.productTypes?.firstOrNull { type ->
            type.name == PRODUCT_TYPE_NAME
        })

        // Get the product type id
        val idResponse = defectDojo.getProductTypes(name = PRODUCT_TYPE_NAME).execute()
        val id = idResponse.body()?.productTypes?.get(0)?.id
        assertNotNull(id)

        // Update the product type
        val productType = ProductType(
            name = PRODUCT_TYPE_NAME_UPDATE,
            id = id
        )
        val response = defectDojo.addProductType(productType).execute()
        when {
            response.isSuccessful -> {
                LOGGER.info("ProductType updated")
            }
            else -> handleResponseError(response)
        }
    }

    /* Delete is disabled for the moment. Uncomment this code once it is reactivated
    @Test
    fun deleteProductType() {
        // Verify that the product type exists
        val productTypesResponse = defectDojo.getProductTypes().execute().body()
        assertNotNull(productTypesResponse)
        assertNotNull(productTypesResponse?.productTypes)
        val productType = productTypesResponse?.productTypes?.firstOrNull { type ->
            type.name == PRODUCT_TYPE_NAME_UPDATE
        }
        assertNotNull(productType)

        // Get the product type id
        val id = productType?.id
        assertNotNull(id)

        // Delete the product type
        val response = defectDojo.deleteProductType(id!!).execute()
        when {
            response.isSuccessful -> {
                LOGGER.info("ProductType deleted")
            }
            else -> handleResponseError(response)
        }
    } */

    @Test
    fun addProduct() {
        val productTypes = defectDojo.getProductTypes().execute().body()?.productTypes
        checkNotNull(productTypes)
        val product = Product(
            // Required
            prodType = productTypes[0].id.toString(),
            // Required
            name = PRODUCT_NAME,
            // Required
            description = "Lorem ipsum",
            created = LocalDateTime.now().toString(),
            internetAccessible = true
        )
        val response = defectDojo.addProduct(product).execute()
        when {
            response.isSuccessful -> {
                LOGGER.info("Product added")
            }
            else -> handleResponseError(response)
        }
    }

    @Test
    fun getProducts() {
        val response = defectDojo.getProducts().execute()
        when {
            response.isSuccessful -> {
                assert(response.body()?.products?.size!! > 0)
                LOGGER.info("Products retrieved :")
                response.body()?.products?.forEach {
                    LOGGER.info(it?.name)
                }
            }
            else -> handleResponseError(response)
        }
    }
    @Test
    fun getProduct() {
        val product = defectDojo.getProducts().execute().body()?.products?.get(0)
        checkNotNull(product)
        val id = product.id
        val name = product.name
        val response = defectDojo.getProduct(id!!).execute()
        when {
            response.isSuccessful -> {
                assert(response.body()?.name == name!!)
                LOGGER.info("Retrieved product : $name")
            }
            else -> handleResponseError(response)
        }
    }

    @Test
    fun updateProduct() {
        // Get an existing product
        val product = defectDojo.getProducts().execute().body()?.products?.get(0)
        checkNotNull(product)
        val id = product.id

        // Modify it
        val updatedProduct = product.copy(name = PRODUCT_NAME_UPDATE, prodType = null)
        val response = defectDojo.updateProduct(id!!, updatedProduct).execute()
        when {
            response.isSuccessful -> {
                LOGGER.info("Product updated")
            }
            else -> handleResponseError(response)
        }
    }

    private fun handleResponseError(response: Response<*>) {
        when {
            response.code() == 401 -> {
                LOGGER.severe("Action unauthorized")
            }
            response.code() == 404 -> {
                LOGGER.severe("Not found")
            }
        }
        LOGGER.severe(response.errorBody().toString())
    }

}