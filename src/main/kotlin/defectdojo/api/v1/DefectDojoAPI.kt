package defectdojo.api.v1

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface DefectDojoAPI {

    /******************************************************************************
     * App analysis
     **************************************************************************** */
    @GET("$APP_ANALYSIS/")
    fun getAppAnalyses(
        @Query("limit") limit : String? = null,
        @Query("offset") offset : String? = null,
        @Query("name") name : String? = null,
        @Query("name__contains") nameContains: String? = null,
        @Query("name__icontains") nameContainsIgnoreCase: String? = null,
        @Query("product__id") productId: String? = null,
        @Query("product__name") productName: String? = null,
        @Query("product__name__icontains") productNameContainsIgnoreCase: String? = null
    ): Call<AppAnalysisGetResponse>

    @GET("$APP_ANALYSIS/{id}/")
    fun getAppAnalysis(
        @Path("id") id: Int
    ): Call<AppAnalysis>


    @POST("$APP_ANALYSIS/")
    fun addAppAnalysis(@Body appAnalysis: AppAnalysis) : Call<Void>

    @PUT("$APP_ANALYSIS/{id}/")
    fun updateAppAnalysis(@Path("id") id:Int, @Body appAnalysis: AppAnalysis) : Call<Void>

    @DELETE("$APP_ANALYSIS/{id}/")
    fun deleteAppAnalysis(@Path("id") id:Int) : Call<Void>

    /******************************************************************************
     * Development Envinronment
     **************************************************************************** */
    @GET("$DEVELOPMENT_ENVIRONMENTS/")
    fun getDevelopmentEnvironments(
        @Query("limit") limit : String? = null,
        @Query("offset") offset : String? = null,
        @Query("name") name : String? = null,
        @Query("name__contains") nameContains: String? = null,
        @Query("name__icontains") nameContainsIgnoreCase: String? = null
    ): Call<DevelopmentEnvironmentGetResponse>

    @GET("$DEVELOPMENT_ENVIRONMENTS/{id}/")
    fun getDevelopmentEnvironment(
        @Path("id") id: Int
    ): Call<DevelopmentEnvironment>


    @POST("$DEVELOPMENT_ENVIRONMENTS/")
    fun addDevelopmentEnvironment(@Body developmentEnvironment: DevelopmentEnvironment) : Call<Void>

    @PUT("$DEVELOPMENT_ENVIRONMENTS/{id}/")
    fun updateDevelopmentEnvironment(@Path("id") id:Int, @Body developmentEnvironment: DevelopmentEnvironment) : Call<Void>

    /******************************************************************************
     * Product Types
     **************************************************************************** */

    @GET("$PRODUCT_TYPES/")
    fun getProductTypes(
        @Query("limit")limit : String? = null,
        @Query("offset")offset : String? = null,
        @Query("name")name : String? = null,
        @Query("name__contains") nameContains: String? = null,
        @Query("name__icontains") nameContainsIgnoreCase: String? = null,
        @Query("order_by") orderBy: String? = null
    ): Call<ProductTypesGetResponse>

    @GET("$PRODUCT_TYPES/{id}")
    fun getProductType(@Path("id") id: Int): Call<ProductType>

    @POST("$PRODUCT_TYPES/")
    fun addProductType(@Body productType: ProductType) : Call<Void>

    @DELETE("$PRODUCT_TYPES/{id}/")
    fun deleteProductType(@Path("id") id: Int): Call<ProductType>

    @PUT("$PRODUCT_TYPES/{id}/")
    fun updateProductType(@Path("id") id: Int, @Body productType: ProductType) : Call<Void>


    /******************************************************************************
     * Products
     **************************************************************************** */

    @GET("$PRODUCTS/")
    fun getProducts(@Query("limit")limit : String? = null,
                    @Query("offset")offset : String? = null,
                    @Query("name")name : String? = null,
                    @Query("name__contains") nameContains: String? = null,
                    @Query("name__icontains") nameContainsIgnoreCase: String? = null,
                    @Query("order_by") orderBy: String? = null
    ): Call<ProductsGetResponse>


    @GET("$PRODUCTS/{id}")
    fun getProduct(@Path("id") productId: Int): Call<Product>

    @POST("$PRODUCTS/")
    fun addProduct(@Body product: Product): Call<Void>

    @PUT("$PRODUCTS/{id}/")
    fun updateProduct(@Path("id") id: Int, @Body product: Product): Call<Void>

    /******************************************************************************
     * Language
     **************************************************************************** */
    @GET("$LANGUAGES/")
    fun getLanguages(
        @Query("limit") limit : String? = null,
        @Query("offset") offset : String? = null,
        @Query("language") name : String? = null,
        @Query("language__contains") nameContains: String? = null,
        @Query("language__icontains") nameContainsIgnoreCase: String? = null,
        @Query("product__id") productId: String? = null,
        @Query("product__name") productName: String? = null,
        @Query("product__name__icontains") productNameContainsIgnoreCase: String? = null
    ): Call<LanguagesGetResponse>

    @GET("$LANGUAGES/{id}/")
    fun getLanguage(
        @Path("id") id: Int
    ): Call<Language>

    @POST("$LANGUAGES/")
    fun addLanguage(@Body language: Language) : Call<Void>

    @DELETE("$LANGUAGES/{id}/")
    fun deleteLanguage(@Path("id") id: Int): Call<Void>

    @PUT("$LANGUAGES/{id}/")
    fun updateLanguage(@Path("id") id: Int, @Body language: Language) : Call<Void>

    /******************************************************************************
     * Language Types
     **************************************************************************** */
    @GET("$LANGUAGE_TYPES/")
    fun getLanguageTypes(
        @Query("limit")limit : String? = null,
        @Query("offset")offset : String? = null,
        @Query("language")name : String? = null,
        @Query("language__contains") nameContains: String? = null,
        @Query("language__icontains") nameContainsIgnoreCase: String? = null
    ): Call<LanguageTypesGetResponse>

    @GET("$LANGUAGE_TYPES/{id}/")
    fun getLanguageType(
        @Path("id") id: Int
    ): Call<LanguageType>

    @POST("$LANGUAGE_TYPES/")
    fun addLanguageType(@Body languageType: LanguageType) : Call<Void>

    @DELETE("$LANGUAGE_TYPES/{id}/")
    fun deleteLanguageType(@Path("id") id: Int): Call<Void>

    @PUT("$LANGUAGE_TYPES/{id}/")
    fun updateLanguageType(@Path("id") id: Int, @Body languageType: LanguageType) : Call<Void>
    
    /******************************************************************************
     * Users
     **************************************************************************** */
    @GET("$USERS/")
    fun getUsers(
        @Query("limit")limit : String? = null,
        @Query("offset")offset : String? = null,
        @Query("username")username : String? = null,
        @Query("username__contains") usernameContains: String? = null,
        @Query("username__icontains") usernameContainsIgnoreCase: String? = null,
        @Query("first_name")firstName : String? = null,
        @Query("first_name__contains") firstNameContains: String? = null,
        @Query("first_name__icontains") firstNameContainsIgnoreCase: String? = null,
        @Query("first_name") lastName : String? = null,
        @Query("last_name__contains") lastNameContains: String? = null,
        @Query("last_name__icontains") lastNameContainsIgnoreCase: String? = null
    ): Call<UsersGetResponse>

    @GET("$USERS/{id}/")
    fun getUser(
        @Path("id") id: Int
    ): Call<User>

    /******************************************************************************
     * Scans
     **************************************************************************** */
    @GET("$SCANS/")
    fun getScans(
        @Query("limit")limit : String? = null,
        @Query("offset")offset : String? = null,
        @Query("settings")settings : String? = null,
        @Query("settings__contains") settingsContains: String? = null,
        @Query("settings__icontains") settingsContainsIgnoreCase: String? = null
    ): Call<ScansGetResponse>

    @GET("$SCANS/{id}/")
    fun getScan(
        @Path("id") id: Int
    ): Call<Scan>

    /******************************************************************************
     * Factory
     **************************************************************************** */
    companion object {
        const val APP_ANALYSIS = "app_analysis"
        const val DEVELOPMENT_ENVIRONMENTS = "development_environments"
        const val LANGUAGES = "languages"
        const val LANGUAGE_TYPES = "language_types"
        const val PRODUCTS = "products"
        const val PRODUCT_TYPES = "product_types"
        const val USERS = "users"
        const val SCANS = "scans"

        private fun createRetrofit(apiBaseUrl: String, username: String, apiKey: String, debug: Boolean): Retrofit {
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val request = original.newBuilder()
                    .header("Authorization", "ApiKey $username:$apiKey")
                    .method(original.method(), original.body())
                    .build()
                chain.proceed(request)
            }
            val interceptor = HttpLoggingInterceptor { msg -> println(msg) }
            if (debug) {
                interceptor.level = HttpLoggingInterceptor.Level.BODY
            }
            httpClient.addInterceptor(interceptor)
            val client = httpClient.build()

            val gson = GsonBuilder()
                .setLenient()
                .create()

            return Retrofit.Builder()
                .baseUrl(apiBaseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        fun create(apiBaseUrl: String, username: String, apiKey: String, debug: Boolean): DefectDojoAPI {
            return createRetrofit(apiBaseUrl, username, apiKey, debug).create(DefectDojoAPI::class.java)
        }
    }
}