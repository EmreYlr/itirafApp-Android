package com.itirafapp.android.di

import com.google.gson.Gson
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
import com.itirafapp.android.BuildConfig
import com.itirafapp.android.data.remote.network.AuthGuardInterceptor
import com.itirafapp.android.data.remote.network.AuthInterceptor
import com.itirafapp.android.data.remote.auth.AuthService
import com.itirafapp.android.data.remote.network.NetworkLoggerInterceptor
import com.itirafapp.android.data.remote.user.UserService
import com.itirafapp.android.data.repository.AuthRepositoryImpl
import com.itirafapp.android.data.repository.UserRepositoryImpl
import com.itirafapp.android.domain.repository.AuthRepository
import com.itirafapp.android.domain.repository.UserRepository
import com.itirafapp.android.util.TokenManager
import com.itirafapp.android.util.UserManager

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(
        authInterceptor: AuthInterceptor,
        authGuardInterceptor: AuthGuardInterceptor,
        loggingInterceptor: HttpLoggingInterceptor,
        networkLoggerInterceptor: NetworkLoggerInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
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
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

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
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService {
        return retrofit.create(UserService::class.java)
    }

    @Provides
    @Singleton
    fun provideAuthRepository(
        api: AuthService,
        tokenManager: TokenManager
    ): AuthRepository {
        return AuthRepositoryImpl(api, tokenManager)
    }

    @Provides
    @Singleton
    fun provideUserRepository(
        userApi: UserService,
        userManager: UserManager
    ): UserRepository {
        return UserRepositoryImpl(userApi, userManager)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson = Gson()
}