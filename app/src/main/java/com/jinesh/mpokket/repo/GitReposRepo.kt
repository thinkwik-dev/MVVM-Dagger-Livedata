package com.jinesh.mpokket.repo

import com.jinesh.mpokket.extension.logv
import com.jinesh.mpokket.network.RetrofitAPI
import com.jinesh.mpokket.network.RetrofitClient
import com.jinesh.mpokket.entity.SearchResponse
import com.jinesh.mpokket.entity.Owner
import com.jinesh.mpokket.entity.Repo
import com.jinesh.mpokket.storage.DBRepoHelper
import com.jinesh.mpokket.network.myGson
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class GitReposRepo @Inject constructor(){
    suspend fun searchGitHubRepo(query:String,sort:String,isAscending:Boolean,success: (ArrayList<Repo>) -> Unit = { },failed: (String) -> Unit = { }) =
        withContext(Dispatchers.IO){
            val call = RetrofitClient.getClient()
                .create(RetrofitAPI::class.java)
                .searchRepositories(query,sort.toLowerCase(),if (isAscending)"asc" else "desc")
            call.enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    failed(t.localizedMessage)
                }
                override  fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    logv("realm====> ${response.code()}}")
                    if (response.isSuccessful){
                        when (response.code()){
                            200->{
                                val response =Gson().fromJson(response.body(), SearchResponse::class.java)
                                response.items?.let {
                                    DBRepoHelper()
                                        .updateRepositoriesInformation(it)
                                    success(it)
                                }
                            }else->{
                            failed(response.message())
                        }
                        }
                    }else{
                        failed(response.message())
                    }

                }

            })
        }




    suspend fun getContributerRepos(login:String, success: (ArrayList<Repo>) -> Unit = { }, failure: (String) -> Unit = { }) =
        withContext(Dispatchers.IO){
            val call = RetrofitClient.getClient()
                .create(RetrofitAPI::class.java)
                .getRepos(login = login)
            call.enqueue(object : Callback<JsonArray> {
                override fun onFailure(call: Call<JsonArray>, t: Throwable) {
                    failure(t.localizedMessage)
                }
                override  fun onResponse(
                    call: Call<JsonArray>,
                    response: Response<JsonArray>
                ) {
                    if (response.isSuccessful){
                        when (response.code()){
                            200->{
                                val typeToken = object : TypeToken<ArrayList<Repo>>() {}
                                val contributers = myGson().fromJson<ArrayList<Repo>>(
                                    (response.body()),
                                    typeToken.type
                                )
                                success(contributers)
                            }else ->{
                            failure(response.message())
                        }

                        }
                    }else{
                        failure(response.message())
                    }
                }

            })
        }


    suspend fun getUserDetail(login:String, success: (Owner) -> Unit = { }, failure: (String) -> Unit = { }) =
        withContext(Dispatchers.IO){
            val call = RetrofitClient.getClient()
                .create(RetrofitAPI::class.java)
                .getUserDetail(login = login)
            call.enqueue(object : Callback<JsonObject> {
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    failure(t.localizedMessage)
                }
                override  fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {
                    if (response.isSuccessful){
                        when (response.code()){
                            200->{
                                val response =Gson().fromJson(response.body(), Owner::class.java)
                                success(response)
                            }else->{
                            failure(response.message())
                        }

                        }
                    }else{
                        failure(response.message())
                    }
                }

            })
        }


}

