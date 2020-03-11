package com.jinesh.mpokket

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import com.bedwal.bijak.mvp.injection.module.ContextModule
import com.jinesh.mpokket.dagger.component.DaggerUtilComponent
import com.jinesh.mpokket.dagger.component.UtilComponent
import com.facebook.stetho.Stetho
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.support.HasSupportFragmentInjector
import io.realm.Realm
import io.realm.RealmConfiguration
import javax.inject.Inject


class MyApplication : Application(), HasSupportFragmentInjector, HasActivityInjector,
    LifecycleObserver {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    companion object {
        lateinit var instance: MyApplication
        lateinit var component: UtilComponent
        fun realmInstance(): Realm {
            return Realm.getDefaultInstance()
        }
    }

    override fun onCreate() {
        super.onCreate()
        initDagger()
        setUpRealm()
        Stetho.initializeWithDefaults(this);
    }

    init {
        instance = this
    }

    private fun initDagger() {
        //Dagger
        component = DaggerUtilComponent
            .builder()
            .contextModule(ContextModule(this))
            .build()

        component.inject(this)
    }

    private fun setUpRealm() {
        Realm.init(this)
        val mRealmConfiguration = RealmConfiguration.Builder()
            .name("local.realm")
            .schemaVersion(1) // skip if you are not managing
            .deleteRealmIfMigrationNeeded()
            .build()

        Realm.getInstance(mRealmConfiguration)
        Realm.setDefaultConfiguration(mRealmConfiguration)
        realmInstance()
    }

    override fun activityInjector(): AndroidInjector<Activity> = activityInjector

    override fun supportFragmentInjector(): AndroidInjector<Fragment> = fragmentInjector

}