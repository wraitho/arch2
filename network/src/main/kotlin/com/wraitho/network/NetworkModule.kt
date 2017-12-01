package com.wraitho.network

import com.google.gson.Gson
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

@Module
class NetworkModule {

    private val timeout = 5000L
    private val apiUrl = "https://api.github.com"

    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                    .baseUrl(apiUrl)
                    .client(okHttpClient)
                    .addConverterFactory(MoshiConverterFactory
                            .create(Moshi.Builder().add(KotlinJsonAdapterFactory()).build()))
                    .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .build()

    @Provides
    fun providesOkHttpClient(): OkHttpClient {
        val builder = OkHttpClient.Builder()

        builder.connectTimeout(timeout, TimeUnit.MILLISECONDS)
                .readTimeout(timeout, TimeUnit.MILLISECONDS)
                .writeTimeout(timeout, TimeUnit.MILLISECONDS)

        if (BuildConfig.DEBUG) {
            builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
        }

        return builder.build()
    }
}
