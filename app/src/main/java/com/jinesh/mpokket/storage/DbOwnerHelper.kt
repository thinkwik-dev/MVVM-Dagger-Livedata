package com.jinesh.mpokket.storage

import com.jinesh.mpokket.MyApplication
import com.jinesh.mpokket.entity.Repo
import com.jinesh.mpokket.entity.Owner
import io.realm.RealmList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DbOwnerHelper @Inject constructor(){

      fun updateRepo(repo: Repo) {
        val realm = MyApplication.realmInstance()
        realm.executeTransactionAsync {
            realm.insertOrUpdate(repo)
        }
        realm.close()
    }
    fun updateOwnerInformation(items: ArrayList<Owner>) {
        val realm = MyApplication.realmInstance()
        realm.executeTransactionAsync {
            val realmList = RealmList<Owner>()
            realmList.addAll(items)
            it.insertOrUpdate(realmList)
        }
        realm.close()
    }
      suspend fun getOwner(id:String,result: (Owner) -> Unit = { }) =
        withContext(Dispatchers.IO) {
            val realm = MyApplication.realmInstance()
            realm.executeTransactionAsync {
                val dbResult  = it.where(Owner::class.java).equalTo("id", id).findFirst()
                dbResult?.let {
                     result(dbResult)
                }
            }
            realm.close()
        }


}