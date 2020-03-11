package com.jinesh.mpokket.modules.search.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jinesh.mpokket.entity.Repo
import com.jinesh.mpokket.storage.DBRepoHelper
import com.jinesh.mpokket.repo.GitReposRepo
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchRepoViewModel@Inject
constructor(var repo: GitReposRepo, var storage: DBRepoHelper) :
    ViewModel() {

    private val searchLiveData = MutableLiveData<ArrayList<Repo>>()
    private val isResultNotFound = MutableLiveData<Boolean>()
    private val sort = MutableLiveData<String>()
    private val isLoading = MutableLiveData<Boolean>()
    private var query: String = ""
    private val isAscending = MutableLiveData<Boolean>()

    fun  int() {
        isAscending.postValue(true)
        sort.postValue("")
    }

    fun searchKeyword(q: String){
        this.query = q
        searchResult()
    }
    fun sortBy(sort: String){
        this.sort.postValue(sort)
        searchResult()
    }

    fun orderBy(){
        if (isAscending.value == true ){
            isAscending.postValue(false)
        }else {
            isAscending.postValue(true)
        }
        searchResult()
    }

    private fun searchResult() {
        viewModelScope.launch {
            isLoading.postValue(true)
            repo.searchGitHubRepo(
                query,
                sort.value!!,
                isAscending = isAscending.value!!,
                success = {result->
                    searchLiveData.postValue(result)
                    isLoading.postValue(false)
                    isResultNotFound.postValue(result.isEmpty())
                },
                failed = {message->
                    isLoading.postValue(false)
                    getOfflineData() })
        }
    }

    private fun getOfflineData() {
        viewModelScope.launch {
            storage.getRepositoriesByQuery(query) { result ->
                isResultNotFound.postValue(result.isEmpty())
                searchLiveData.postValue(result)
            }
        }
    }

    fun observeRepoResult(): MutableLiveData<ArrayList<Repo>> {
        return searchLiveData
    }
    fun observeResultNotFound(): MutableLiveData<Boolean> {
        return isResultNotFound
    }
    fun observeSearchLoading(): MutableLiveData<Boolean> {
        return isLoading
    }
    fun observeOrderBy(): MutableLiveData<Boolean> {
        return isAscending
    }
    fun observeSortBy(): MutableLiveData<String> {
        return sort
    }

}
