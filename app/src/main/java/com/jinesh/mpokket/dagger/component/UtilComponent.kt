package com.jinesh.mpokket.dagger.component

import com.bedwal.bijak.mvp.injection.module.ContextModule
import com.bedwal.bijak.mvp.injection.module.LifeCycleModule
import com.jinesh.mpokket.MyApplication
import com.jinesh.mpokket.dagger.modules.BaseModule
import com.jinesh.mpokket.dagger.modules.Modules
import com.jinesh.mpokket.dagger.modules.NetworkModule
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ContextModule::class,
        NetworkModule::class,
        AndroidInjectionModule::class,
        LifeCycleModule::class,
        BaseModule::class,
        Modules::class
    ]
)
interface UtilComponent {
    fun inject(app: MyApplication)
}