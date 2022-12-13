package com.reunet.app.services

import com.google.android.datatransport.runtime.dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

class ProvideToken {
    @Provides
    @Singleton
    fun providesOkhttpClient(token:String): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder().addInterceptor(AuthInterceptor("Bearer",token )
        ).addInterceptor(interceptor)
            .build()
    }
}