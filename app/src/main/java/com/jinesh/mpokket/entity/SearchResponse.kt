package com.jinesh.mpokket.entity

import java.io.Serializable


class SearchResponse : Serializable {
    var total_count: String? = null
    var incomplete_results: String? = null
    var items: ArrayList<Repo>? = null
}