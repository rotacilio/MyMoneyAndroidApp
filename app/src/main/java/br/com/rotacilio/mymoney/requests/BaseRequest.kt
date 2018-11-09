package br.com.rotacilio.mymoney.requests

import android.content.Context
import br.com.rotacilio.mymoney.commons.CallbackRequest
import br.com.rotacilio.mymoney.commons.Constants
import br.com.rotacilio.mymoney.commons.InternetConnection
import br.com.rotacilio.mymoney.requests.services.BrandsServices
import br.com.rotacilio.mymoney.requests.services.CardsServices
import com.google.gson.GsonBuilder
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

abstract class BaseRequest {

    var mCardsServices: CardsServices? = null
    var mBrandsServices: BrandsServices? = null
    var mCallbackRequest: CallbackRequest? = null
    var mRetrofit: Retrofit? = null

    fun call(callbackRequest: CallbackRequest, context: Context) {
        this.mCallbackRequest = callbackRequest

        val cacheSize = 10 * 1024 * 1024
        val cache = Cache(context.cacheDir, cacheSize.toLong())

        val okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(object : Interceptor {
                    override fun intercept(chain: Interceptor.Chain): Response {
                        if (InternetConnection.internetAvailable(context)) {
                            val originalResponse = chain.proceed(chain.request())
                            val cacheControl = CacheControl.Builder()
                                    .maxAge(5, TimeUnit.MINUTES)
                                    .build()
                            return originalResponse.newBuilder()
                                    .header("Cache-Control", cacheControl.toString())
                                    .build()
                        } else {
                            val request = chain.request()
                            val cacheControl = CacheControl.Builder()
                                    .maxStale(1, TimeUnit.DAYS)
                                    .build()
                            request.newBuilder()
                                    .cacheControl(cacheControl)
                                    .build()
                            return chain.proceed(request)
                        }
                    }
                })
                .cache(cache)
                .build()

        val gson = GsonBuilder()
                .setLenient()
                .create()

        mRetrofit = Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

        mCardsServices = mRetrofit?.create(CardsServices::class.java)
        mBrandsServices = mRetrofit?.create(BrandsServices::class.java)
    }
}