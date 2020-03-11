package com.jinesh.mpokket.storage

import com.jinesh.mpokket.MyApplication
import com.jinesh.mpokket.entity.Repo
import io.realm.Case
import io.realm.RealmList
import io.realm.RealmResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DBRepoHelper @Inject constructor() {

      fun updateRepositoriesInformation(items: ArrayList<Repo>) {
        val realm = MyApplication.realmInstance()
        realm.executeTransactionAsync {
            val realmList = RealmList<Repo>()
            realmList.addAll(items)
            it.insertOrUpdate(realmList)
        }
        realm.close()
    }

      suspend fun getRepositoriesByQuery(q:String, updatedData: (ArrayList<Repo>) -> Unit = { }) =
        withContext(Dispatchers.IO) {
            val realm = MyApplication.realmInstance()
            realm.executeTransactionAsync {
                val post: RealmResults<Repo> = it.where(Repo::class.java).contains("full_name",q.toLowerCase(),Case.INSENSITIVE).findAll()
                val list  = it.copyFromRealm(post) as ArrayList<Repo>
                updatedData(list)
            }
            realm.close()
        }


}