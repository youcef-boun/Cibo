package com.youcef_bounaas.cibo.di

import com.youcef_bounaas.cibo.data.repository.MenuRepository
import com.youcef_bounaas.cibo.data.repository.MenuRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindMenuRepository(impl: MenuRepositoryImpl): MenuRepository
}