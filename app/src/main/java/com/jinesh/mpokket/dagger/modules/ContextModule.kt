package com.bedwal.bijak.mvp.injection.module

import android.app.Application
import android.content.Context
import com.jinesh.mpokket.MyApplication
import dagger.Module
import dagger.Provides

@Module
class ContextModule(val context: Context) {

    @Provides
    fun provideApplication(): Application {
        return context.applicationContext as Application
    }

    @Provides
    fun provideContext(): Context {
        return context
    }

    @Provides
    fun provideMyApplication(application: Application): MyApplication {
        return MyApplication.instance!!
    }
}