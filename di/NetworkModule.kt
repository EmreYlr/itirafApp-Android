package com.itirafapp.android.di

import com.google.gson.Gson
import com.itirafapp.android.BuildConfig
import com.itirafapp.android.data.remote.network.api.TokenRefreshApi
import com.itirafapp.android.data.remote.network.interceptor.AuthGuardInterceptor
import com.itirafapp.android.data.remote.network.interceptor.AuthInterceptor
import com.itirafapp.android.data.remote.network.interceptor.ClientHeadersInterceptor
import com.itirafapp.android.data.remote.network.interceptor.NetworkLoggerInterceptor
import com.itirafapp.android.data.remote.network.interceptor.TokenInterceptor
import com.itirafapp.android.util.manager.SessionEventBus
import com.itirafapp.android.util.manager.TokenManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    @Named("WebSocketUrl")
    fun provideWebSocketUrl(): String = BuildConfig.WS_URL

    @Provides
    @Singleton
    @Named("ClientKey")
    fun provideClientKey(): String = BuildConfig.CLIENT_KEY

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideClientHeadersInterceptor(
        @Named("ClientKey") clientKey: String
    ): ClientHeadersInterceptor {
        return ClientHeadersInterceptor(clientKey)
    }

    @Provides
    @Singleton
    fun provideTokenInterceptor(
        tokenManager: TokenManager,
        authApiProvider: TokenRefreshApi,
        sessionEventBus: SessionEventBus
    ): TokenInterceptor {
        return TokenInterceptor(tokenManager, authApiProvider, sessionEventBus)
    }


    @Provides
    @Singleton
    fun provideOkHttpClient(
        clientHeadersInterceptor: ClientHeadersInterceptor,
        authInterceptor: AuthInterceptor,
        tokenInterceptor: TokenInterceptor,
        authGuardInterceptor: AuthGuardInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        networkLoggerInterceptor: NetworkLoggerInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(clientHeadersInterceptor)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(authGuardInterceptor)
            .addInterceptor(authInterceptor)
            .addInterceptor(networkLoggerInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    @Named("RefreshOkHttpClient")
    fun provideRefreshOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        networkLoggerInterceptor: NetworkLoggerInterceptor,
        clientHeadersInterceptor: ClientHeadersInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(networkLoggerInterceptor)
            .addInterceptor(clientHeadersInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideTokenRefreshApi(
        @Named("RefreshOkHttpClient") okHttpClient: OkHttpClient
    ): TokenRefreshApi {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TokenRefreshApi::class.java)
    }
}