package com.itirafapp.android.di

import com.itirafapp.android.data.repository.AuthRepositoryImpl
import com.itirafapp.android.data.repository.ChannelRepositoryImpl
import com.itirafapp.android.data.repository.ChatSocketRepositoryImpl
import com.itirafapp.android.data.repository.ConfessionRepositoryImpl
import com.itirafapp.android.data.repository.CrashReporterImpl
import com.itirafapp.android.data.repository.DeviceRepositoryImpl
import com.itirafapp.android.data.repository.FollowRepositoryImpl
import com.itirafapp.android.data.repository.LanguageRepositoryImpl
import com.itirafapp.android.data.repository.NotificationRepositoryImpl
import com.itirafapp.android.data.repository.RoomRepositoryImpl
import com.itirafapp.android.data.repository.SocialLinkRepositoryImpl
import com.itirafapp.android.data.repository.ThemeRepositoryImpl
import com.itirafapp.android.data.repository.UserRepositoryImpl
import com.itirafapp.android.domain.repository.AuthRepository
import com.itirafapp.android.domain.repository.ChannelRepository
import com.itirafapp.android.domain.repository.ChatSocketRepository
import com.itirafapp.android.domain.repository.ConfessionRepository
import com.itirafapp.android.domain.repository.CrashReporter
import com.itirafapp.android.domain.repository.DeviceRepository
import com.itirafapp.android.domain.repository.FollowRepository
import com.itirafapp.android.domain.repository.LanguageRepository
import com.itirafapp.android.domain.repository.NotificationRepository
import com.itirafapp.android.domain.repository.RoomRepository
import com.itirafapp.android.domain.repository.SocialLinkRepository
import com.itirafapp.android.domain.repository.ThemeRepository
import com.itirafapp.android.domain.repository.UserRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        userRepositoryImpl: UserRepositoryImpl
    ): UserRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl
    ): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindConfessionRepository(
        confessionRepositoryImpl: ConfessionRepositoryImpl
    ): ConfessionRepository

    @Binds
    @Singleton
    abstract fun bindRoomRepository(
        roomRepositoryImpl: RoomRepositoryImpl
    ): RoomRepository

    @Binds
    @Singleton
    abstract fun bindChannelRepository(
        channelRepositoryImpl: ChannelRepositoryImpl
    ): ChannelRepository

    @Binds
    @Singleton
    abstract fun bindFollowRepository(
        followRepositoryImpl: FollowRepositoryImpl
    ): FollowRepository

    @Binds
    @Singleton
    abstract fun bindSocialLinkRepository(
        socialLinkRepositoryImpl: SocialLinkRepositoryImpl
    ): SocialLinkRepository

    @Binds
    @Singleton
    abstract fun bindThemeRepository(
        themeRepositoryImpl: ThemeRepositoryImpl
    ): ThemeRepository

    @Binds
    @Singleton
    abstract fun bindLanguageRepository(
        languageRepositoryImpl: LanguageRepositoryImpl
    ): LanguageRepository

    @Binds
    @Singleton
    abstract fun bindDeviceRepository(
        deviceRepositoryImpl: DeviceRepositoryImpl
    ): DeviceRepository

    @Binds
    @Singleton
    abstract fun bindChatSocketRepository(
        chatSocketRepositoryImpl: ChatSocketRepositoryImpl
    ): ChatSocketRepository

    @Binds
    @Singleton
    abstract fun bindCrashReporter(
        crashReporterImpl: CrashReporterImpl
    ): CrashReporter
}