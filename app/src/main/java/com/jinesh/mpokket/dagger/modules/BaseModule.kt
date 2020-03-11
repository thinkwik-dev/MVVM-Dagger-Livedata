package com.jinesh.mpokket.dagger.modules

import com.jinesh.mpokket.base.BaseFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class BaseModule {
    @ContributesAndroidInjector
    abstract fun contributeBaseFragmentInjector(): BaseFragment
}
