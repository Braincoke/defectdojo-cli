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
    @GET("app_analysis/")
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

    @GET("app_analysis/{id}/")
    fun getAppAnalysis(
        @Path("id") id: Int
    ): Call<AppAnalysis>


    @POST("app_analysis/")
    fun addAppAnalysis(@Body appAnalysis: AppAnalysis) : Call<Void>

    @PUT("app_analysis/{id}/")
    fun updateAppAnalysis(@Path("id") id:Int, @Body appAnalysis: AppAnalysis) : Call<Void>

    @DELETE("app_analysis/{id}/")
    fun deleteAppAnalysis(@Path("id") id:Int) : Call<Void>

    /******************************************************************************
     * Development Envinronment
     **************************************************************************** */
    @GET("development_environments/")
    fun getDevelopmentEnvironments(
        @Query("limit") limit : String? = null,
        @Query("offset") offset : String? = null,
        @Query("name") name : String? = null,
        @Query("name__contains") nameContains: String? = null,
        @Query("name__icontains") nameContainsIgnoreCase: String? = null
    ): Call<DevelopmentEnvironmentGetResponse>

    @GET("development_environments/{id}/")
    fun getDevelopmentEnvironment(
        @Path("id") id: Int
    ): Call<DevelopmentEnvironment>


    @POST("development_environments/")
    fun addDevelopmentEnvironment(@Body developmentEnvironment: DevelopmentEnvironment) : Call<Void>

    @PUT("development_environments/{id}/")
    fun updateDevelopmentEnvironment(@Path("id") id:Int, @Body developmentEnvironment: DevelopmentEnvironment) : Call<Void>

    /******************************************************************************
     * Product Types
     **************************************************************************** */

    @GET("product_types/")
    fun getProductTypes(
        @Query("limit")limit : String? = null,
        @Query("offset")offset : String? = null,
        @Query("name")name : String? = null,
        @Query("name__contains") nameContains: String? = null,
        @Query("name__icontains") nameContainsIgnoreCase: String? = null,
        @Query("order_by") orderBy: String? = null
    ): Call<ProductTypesGetResponse>

    @GET("product_types/{id}")
    fun getProductType(@Path("id") id: Int): Call<ProductType>

    @POST("product_types/")
    fun addProductType(@Body productType: ProductType) : Call<Void>

    @DELETE("product_types/{id}")
    fun deleteProductType(@Path("id") id: Int): Call<ProductType>

    @PUT("product_types/{id}/")
    fun updateProductType(@Path("id") id: Int, @Body productType: ProductType) : Call<Void>


    /******************************************************************************
     * Products
     **************************************************************************** */

    @GET("products/")
    fun getProducts(@Query("limit")limit : String? = null,
                    @Query("offset")offset : String? = null,
                    @Query("name")name : String? = null,
                    @Query("name__contains") nameContains: String? = null,
                    @Query("name__icontains") nameContainsIgnoreCase: String? = null,
                    @Query("order_by") orderBy: String? = null
    ): Call<ProductsGetResponse>


    @GET("products/{id}")
    fun getProduct(@Path("id") productId: Int): Call<Product>

    @POST("products/")
    fun addProduct(@Body product: Product): Call<Void>

    @PUT("products/{id}/")
    fun updateProduct(@Path("id") id: Int, @Body product: Product): Call<Void>

    /******************************************************************************
     * Language
     **************************************************************************** */
    @GET("languages/")
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

    @GET("languages/{id}/")
    fun getLanguage(
        @Path("id") id: Int
    ): Call<Language>


    /******************************************************************************
     * Language Types
     **************************************************************************** */
    @GET("language_types/")
    fun getLanguageTypes(
        @Query("limit")limit : String? = null,
        @Query("offset")offset : String? = null,
        @Query("language")name : String? = null,
        @Query("language__contains") nameContains: String? = null,
        @Query("language__icontains") nameContainsIgnoreCase: String? = null
    ): Call<LanguageTypesGetResponse>

    @GET("language_types/{id}/")
    fun getLanguageType(
        @Path("id") id: Int
    ): Call<LanguageType>

    /******************************************************************************
     * Users
     **************************************************************************** */
    @GET("users/")
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

    @GET("users/{id}/")
    fun getUser(
        @Path("id") id: Int
    ): Call<User>

    /******************************************************************************
     * Factory
     **************************************************************************** */
    companion object {

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