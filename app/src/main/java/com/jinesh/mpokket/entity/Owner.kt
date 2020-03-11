package com.jinesh.mpokket.entity

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import java.io.Serializable

open class Owner(
    @PrimaryKey var id: String? = null,
    var login: String? = null,
    var gists_url: String? = null,
    var repos_url: String? = null,
    var followers_url: String? = null,
    var type: String? = null,
    var url: String? = null,
    var subscriptions_url: String? = null,
    var received_events_url: String? = null,
    var avatar_url: String? = null,
    var events_url: String? = null,
    var html_url: String? = null,
    var site_admin: String? = null,
    var gravatar_id: String? = null,
    var node_id: String? = null,
    var name: String? = null,
    var location: String? = null,
    var blog: String? = null,
    var followers: String? = null,
    var following: String? = null,
    var contributions: Long = 0,
    var company: String? = null,
    var organizations_url: String? = null
) : RealmObject(),Serializable