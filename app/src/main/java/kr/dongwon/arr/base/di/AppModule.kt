package kr.dongwon.arr.base.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kr.dongwon.arr.base.AppApi
import kr.dongwon.arr.base.Const
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSharedPreference(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(Const.PREFERENCES_NAME, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideApi(retrofit: Retrofit): AppApi = retrofit.create(AppApi::class.java)

    @Singleton
    @Provides
    fun provideInterceptor (): Interceptor {
        return Interceptor {
            it.proceed(
                it.request().newBuilder()
                    .addHeader("accept", "application/json")
                    .addHeader("Content-type", "application/json")
                    .build()
            )
        }
    }

    @Singleton
    @Provides
    fun provideOKHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(provideInterceptor()).build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).client(okHttpClient).baseUrl(Const.BASE_URL).build()
    }
}