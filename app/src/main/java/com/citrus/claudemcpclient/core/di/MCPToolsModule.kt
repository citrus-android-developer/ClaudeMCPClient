package com.citrus.claudemcpclient.core.di

import com.citrus.claudemcpclient.core.network.MCPToolsService
import com.citrus.claudemcpclient.data.repository.MCPToolsRepository
import com.citrus.claudemcpclient.data.datasource.MCPToolsRemoteDataSource
import com.citrus.claudemcpclient.domain.usecase.MCPToolsUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MCPToolsModule {
    @Provides
    @Singleton
    fun provideMCPToolsService(): MCPToolsService = MCPToolsService()

    @Provides
    @Singleton
    fun provideMCPToolsRemoteDataSource(service: MCPToolsService): MCPToolsRemoteDataSource =
        MCPToolsRemoteDataSource(service)

    @Provides
    @Singleton
    fun provideMCPToolsRepository(dataSource: MCPToolsRemoteDataSource): MCPToolsRepository =
        MCPToolsRepository(dataSource)

    @Provides
    @Singleton
    fun provideMCPToolsUseCase(repository: MCPToolsRepository): MCPToolsUseCase =
        MCPToolsUseCase(repository)
}