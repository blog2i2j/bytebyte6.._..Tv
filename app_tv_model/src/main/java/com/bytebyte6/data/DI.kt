package com.bytebyte6.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.bytebyte6.base.GsonConfig
import com.bytebyte6.data.work.AppDelegatingWorkerFactory
import com.bytebyte6.data.work.CountryImageSearch
import com.bytebyte6.data.work.ImageSearch
import com.bytebyte6.data.work.TvLogoSearch
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 实际环境使用
 */
val roomModule = module {
    single { createDb(androidApplication()) }
}

/**
 * 测试专用
 */
val roomMemoryModule = module {
    single<Context> { ApplicationProvider.getApplicationContext<Context>() }
    single { Room.inMemoryDatabaseBuilder(get(), AppDatabase::class.java).build() }
}

val dataModule = module {
    single { createRetrofit(get()) }
    single<TvApi> { get(Retrofit::class.java).create(TvApi::class.java) }
    single { get(AppDatabase::class.java).tvDao() }
    single { get(AppDatabase::class.java).tvFtsDao() }
    single { get(AppDatabase::class.java).userDao() }
    single { get(AppDatabase::class.java).playlistDao() }
    single { get(AppDatabase::class.java).countryDao() }
    single { AppDelegatingWorkerFactory(get(), get()) }
    single { CountryImageSearch(get()) }
    single { TvLogoSearch(get()) }
    single { get(AppDatabase::class.java).playlistTvCrossRefDao() }
    single { get(AppDatabase::class.java).userPlaylistCrossRefDao() }
    factory { ImageSearch() }
    factory {
        GsonBuilder().registerTypeAdapterFactory(GsonConfig.NullStringToEmptyAdapterFactory())
            .create()
    }
    factory { GsonConverterFactory.create(get()) }
}

private fun createDb(context: Context): AppDatabase {
    return Room.databaseBuilder(context, AppDatabase::class.java, "rtmp.db").build()
}

private fun createRetrofit(gsonConverterFactory: GsonConverterFactory): Retrofit {
    return Retrofit.Builder()
        .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }).build())
        .baseUrl("https://iptv-org.github.io/iptv/")
        .addConverterFactory(gsonConverterFactory)
        .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
        .build()
}

