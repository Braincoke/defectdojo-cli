package defectdojo.api.v1

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


interface DefectDojoAPI {

    /* App analysis (Technology) */

    @GET("app_analysis/")
    fun getAppAnalyses(): Call<AppAnalysisList>

    @GET("app_analysis/{id}")
    fun getAppAnalysis(@Path("id") appAnalysisId: Int): Call<AppAnalysis>

    @POST("app_analysis/")
    fun postAppAnalysis()

    /******************************************************************************
     * Products
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

    @PUT("product_types/{id}")
    fun updateProductType(@Path("id") id: Int, @Body productType: ProductType) : Call<Void>

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