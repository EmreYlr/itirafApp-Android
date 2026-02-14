package com.itirafapp.android.di

import com.itirafapp.android.data.remote.auth.AuthService
import com.itirafapp.android.data.remote.channel.ChannelService
import com.itirafapp.android.data.remote.confession.ConfessionService
import com.itirafapp.android.data.remote.device.DeviceService
import com.itirafapp.android.data.remote.moderation.ModerationService
import com.itirafapp.android.data.remote.notification.NotificationService
import com.itirafapp.android.data.remote.room.RoomService
import com.itirafapp.android.data.remote.social_link.SocialLinkService
import com.itirafapp.android.data.remote.user.UserService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService =
        retrofit.create(AuthService::class.java)

    @Provides
    @Singleton
    fun provideUserService(retrofit: Retrofit): UserService =
        retrofit.create(UserService::class.java)

    @Provides
    @Singleton
    fun provideNotificationService(retrofit: Retrofit): NotificationService =
        retrofit.create(NotificationService::class.java)

    @Provides
    @Singleton
    fun provideConfessionService(retrofit: Retrofit): ConfessionService =
        retrofit.create(ConfessionService::class.java)

    @Provides
    @Singleton
    fun provideRoomService(retrofit: Retrofit): RoomService =
        retrofit.create(RoomService::class.java)

    @Provides
    @Singleton
    fun provideChannelService(retrofit: Retrofit): ChannelService =
        retrofit.create(ChannelService::class.java)

    @Provides
    @Singleton
    fun provideDeviceService(retrofit: Retrofit): DeviceService =
        retrofit.create(DeviceService::class.java)

    @Provides
    @Singleton
    fun provideSocialLinkService(retrofit: Retrofit): SocialLinkService =
        retrofit.create(SocialLinkService::class.java)

    @Provides
    @Singleton
    fun provideModerationService(retrofit: Retrofit): ModerationService =
        retrofit.create(ModerationService::class.java)
}