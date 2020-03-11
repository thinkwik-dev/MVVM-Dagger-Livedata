package com.jinesh.mpokket.modules.search.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.jinesh.mpokket.R
import com.jinesh.mpokket.base.BaseFragment
import com.jinesh.mpokket.entity.Repo
import com.jinesh.mpokket.extension.setDebounce
import com.jinesh.mpokket.extension.setVisiblity
import com.jinesh.mpokket.modules.repodetail.view.RepositoryDetailFragment
import com.jinesh.mpokket.modules.search.viewmodel.SearchRepoViewModel
import kotlinx.android.synthetic.main.fragment_search_result.*


class SearchRepoFragment : BaseFragment() {

    private val viewModel: SearchRepoViewModel by activityViewModels {
        viewModelFactory
    }

    lateinit var navController: NavController
    lateinit var repoAdapter: RepoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        observer()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_search_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        viewModel.int()
        initData()
        initClickListener()
    }

    private fun initClickListener() {
        etSearch.setDebounce {
            viewModel.searchKeyword(etSearch.text.toString())
        }
        ivFilter.setOnClickListener {
            openSortByFunction(ivFilter)
        }
        tvOrderBy.setOnClickListener {
            viewModel.orderBy()
        }
    }

    private fun initData() {
        repoAdapter = RepoAdapter(activity!!)
        rvPostsss.adapter = repoAdapter
        repoAdapter.addItemClickListener { repo ->
            Navigation.createNavigateOnClickListener(R.id.repoDetailFragment, null)
            viewPostDetail(repo)
        }
    }

    private fun observer() {
        viewModel.observeRepoResult().observe(this, Observer { result ->
            updateSearchResult(result)
        })
        viewModel.observeResultNotFound().observe(this, Observer { result ->
            isResultNotFound(result)
        })
        viewModel.observeSearchLoading().observe(this, Observer { result ->
            if (result) resetAdapter()
            showLoading(result)
        })
        viewModel.observeOrderBy().observe(this, Observer { result ->
            tvOrderBy.text =  if (result) "ASC" else "DESC"
        })
        viewModel.observeSortBy().observe(this, Observer { result ->
            ivFilter.text =  if (result.isNotEmpty())result else "Sort By"
        })
    }


    private fun updateSearchResult(list: ArrayList<Repo>) {
        list.let {
            repoAdapter.data.clear()
            repoAdapter.data.addAll(list)
            repoAdapter.notifyDataSetChanged()
        }
    }

    private fun resetAdapter() {
        repoAdapter.data.clear()
        repoAdapter.notifyDataSetChanged()
    }

    private fun openSortByFunction(view: View) {
        val popup = PopupMenu(activity, view)
        popup.menuInflater
            .inflate(R.menu.popup_menu, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            viewModel.sortBy(item.title.toString())
            true
        }
        popup.show() //showing popup menu

    }

    private fun showLoading(isLoading: Boolean) {
        incShimmer.setVisiblity(isLoading)
    }

    private fun isResultNotFound(isResultNotFound: Boolean) {
        groupFilter.setVisiblity(!isResultNotFound)
        tvResultNotFound.setVisiblity(isResultNotFound)
    }

    private fun viewPostDetail(repo: Repo) {
        RepositoryDetailFragment.start(navController, repo)
    }

}
