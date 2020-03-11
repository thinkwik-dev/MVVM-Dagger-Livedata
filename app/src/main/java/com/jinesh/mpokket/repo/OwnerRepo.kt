package com.jinesh.mpokket.repo

import com.jinesh.mpokket.network.RetrofitAPI
import com.jinesh.mpokket.network.RetrofitClient
import com.jinesh.mpokket.entity.Owner
import com.jinesh.mpokket.network.myGson
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import com.jinesh.mpokket.storage.DbOwnerHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class OwnerRepo @Inject constructor(){

    suspend fun getContributers(repo:String, login:String, success: (ArrayList<Owner>) -> Unit = { }, failure: (String) -> Unit = { }) =
        withContext(Dispatchers.IO){
            val call = RetrofitClient.getClient()
                .create(RetrofitAPI::class.java)
                .getRepoDetail(repo = repo,login = login)
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
                                val typeToken = object : TypeToken<ArrayList<Owner>>() {}
                                val contributers = myGson().fromJson<ArrayList<Owner>>(
                                    (response.body()),
                                    typeToken.type
                                )
                                DbOwnerHelper()
                                    .updateOwnerInformation(contributers)
                                success(contributers)
                            }else->{
                            failure(response.message())
                        }

                        }
                    }

                }

            })
        }

}

