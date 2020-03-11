package com.bedwal.bijak.mvp.injection.module

import com.bedwal.bijak.mvp.injection.util.ViewModelProvider
import dagger.Binds
import dagger.Module

@Module
abstract class LifeCycleModule {
    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelProvider): androidx.lifecycle.ViewModelProvider.Factory
}
