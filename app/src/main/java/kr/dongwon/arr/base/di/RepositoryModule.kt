package kr.dongwon.arr.base.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kr.dongwon.arr.base.AppRepository
import kr.dongwon.arr.base.AppRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindAppRepository(appRepositoryImpl: AppRepositoryImpl): AppRepository
}