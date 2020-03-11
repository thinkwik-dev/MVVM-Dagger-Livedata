package com.jinesh.mpokket.modules.contributor.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinesh.mpokket.entity.Owner
import com.jinesh.mpokket.entity.Repo
import com.jinesh.mpokket.repo.GitReposRepo
import com.jinesh.mpokket.storage.DbOwnerHelper
import kotlinx.coroutines.launch
import javax.inject.Inject

class ContributorViewModel @Inject constructor(var repo: GitReposRepo, val db: DbOwnerHelper) :
    ViewModel() {


    private val isReopNotFound = MutableLiveData<Boolean>()
    private val repos = MutableLiveData<ArrayList<Repo>>()
    private val ownerDetail = MutableLiveData<Owner>()
    var owner: Owner? = null

    fun getRepos() {
        viewModelScope.launch {
            repo.getUserDetail(owner!!.login!!, success = {
                ownerDetail.postValue(it)
            }, failure = {
                getOfflineUserData()
            })
        }

        viewModelScope.launch {
            repo.getContributerRepos(owner!!.login!!, success = {
                repos.postValue(it)
            }, failure = {
                isReopNotFound.postValue(true)
            })
        }
    }

    //Try to get user info from local db.
    private fun getOfflineUserData() {
        viewModelScope.launch {
            db.getOwner(owner!!.id!!) { result ->
                ownerDetail.postValue(result)
                isReopNotFound.postValue(true)
            }
        }
    }


    fun observeRepoListData(): MutableLiveData<ArrayList<Repo>> {
        return repos
    }

    fun observeOwnerDetail(): MutableLiveData<Owner> {
        return ownerDetail
    }

    fun observeRepoNotFound(): MutableLiveData<Boolean> {
        return isReopNotFound
    }

}
