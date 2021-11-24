package com.duongnh.beertestdemo.di

import com.duongnh.beertestdemo.network.RetrofitClient
import com.duongnh.data.IRetrofitClient
import com.duongnh.data.repository.GetBeersRepository
import com.duongnh.domain.repository.IGetBeersRepository
import com.duongnh.domain.usecases.GetBeersUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun bindRetrofitClient(): IRetrofitClient = RetrofitClient()

    @Provides
    @Singleton
    fun bindGetBeersRepository(retrofitClient: IRetrofitClient): IGetBeersRepository = GetBeersRepository(retrofitClient)

    @Provides
    @Singleton
    fun bindGetBeersUseCase(getBeersRepository: IGetBeersRepository) = GetBeersUseCase(getBeersRepository)

}