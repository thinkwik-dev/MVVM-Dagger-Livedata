package com.jinesh.mpokket.dagger.modules

import androidx.lifecycle.ViewModel
import com.bedwal.bijak.mvp.injection.util.ViewModelKey
import com.jinesh.mpokket.modules.contributor.view.ContributorDetailFragment
import com.jinesh.mpokket.modules.contributor.viewmodel.ContributorViewModel
import com.jinesh.mpokket.modules.repodetail.view.RepositoryDetailFragment
import com.jinesh.mpokket.modules.repodetail.viewmodel.OwnerViewModel
import com.jinesh.mpokket.modules.search.view.MainActivity
import com.jinesh.mpokket.modules.search.view.SearchRepoFragment
import com.jinesh.mpokket.modules.search.viewmodel.SearchRepoViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class Modules {

    @Binds
    @IntoMap
    @ViewModelKey(SearchRepoViewModel::class)
    abstract fun bindSearchRepoViewModel(viewModel: SearchRepoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OwnerViewModel::class)
    abstract fun bindOwnerViewModel(viewModel: OwnerViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ContributorViewModel::class)
    abstract fun bindContributorViewModel(viewModel: ContributorViewModel): ViewModel

    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector
    abstract fun contributeSearchRepoFragment(): SearchRepoFragment

    @ContributesAndroidInjector
    abstract fun contributeOwnerDetailFragment(): RepositoryDetailFragment

    @ContributesAndroidInjector
    abstract fun contributeContributorDetailFragment(): ContributorDetailFragment


}
