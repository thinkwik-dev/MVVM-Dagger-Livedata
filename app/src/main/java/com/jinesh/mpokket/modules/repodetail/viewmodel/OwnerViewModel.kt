package com.jinesh.mpokket.modules.repodetail.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinesh.mpokket.entity.Repo
import com.jinesh.mpokket.entity.Owner
import com.jinesh.mpokket.repo.OwnerRepo
import kotlinx.coroutines.launch
import javax.inject.Inject

class OwnerViewModel @Inject constructor(var repo: OwnerRepo) :
    ViewModel() {

     private val contributor = MutableLiveData<ArrayList<Owner>>()
     var repoObject:Repo? = null
    private val isContributorNotFound = MutableLiveData<Boolean>()

    fun getRepoContributor() {
        viewModelScope.launch {
            repoObject?.let {
                repo.getContributers(repoObject!!.name!!,repoObject!!.owner!!.login!!,success = {
                    contributor.postValue(it)
                },failure = {
                    isContributorNotFound.postValue(true)
                })
            }
        }
    }

    fun observePostsLiveData(): MutableLiveData<ArrayList<Owner>> {
        return contributor
    }

    fun observeContributorNotFound(): MutableLiveData<Boolean> {
        return isContributorNotFound
    }

}
