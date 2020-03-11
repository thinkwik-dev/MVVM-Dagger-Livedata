package com.jinesh.mpokket.modules.contributor.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.jinesh.mpokket.R
import com.jinesh.mpokket.base.BaseFragment
import com.jinesh.mpokket.entity.Owner
import com.jinesh.mpokket.entity.Repo
import com.jinesh.mpokket.extension.glide
import com.jinesh.mpokket.extension.setVisiblity
import com.jinesh.mpokket.modules.contributor.viewmodel.ContributorViewModel
import com.jinesh.mpokket.modules.repodetail.view.RepositoryDetailFragment
import com.jinesh.mpokket.modules.search.view.RepoAdapter
import kotlinx.android.synthetic.main.fragment_contributor_detail.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ContributorDetailFragment : BaseFragment() {

    companion object {
        val ARG_OWNER = "Owner"
        fun start(navController: NavController, item: Owner) {
            val bundle = bundleOf(ARG_OWNER to item)
            navController.navigate(R.id.contributorDetail, bundle)
        }
    }

    private val viewModel: ContributorViewModel by activityViewModels {
        viewModelFactory
    }
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.owner = this.arguments!!.getSerializable(ARG_OWNER) as Owner?
        observer()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_contributor_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initData()
    }

    private fun initData() {
        showRepoLoading(true)
        viewModel.getRepos()
    }

    private fun observer() {
        viewModel.observeRepoListData().observe(this, Observer { result ->
            showRepoLoading(false)
            updateSearchResult(result)
        })

        viewModel.observeOwnerDetail().observe(this, Observer { result ->
            showOwnerLoading(false)
            updateDetail(result)
        })

        viewModel.observeRepoNotFound().observe(this, Observer { result ->
            showOwnerLoading(false)
            tvRepoTitle.setVisiblity(false)
        })
    }

    private fun updateSearchResult(list: ArrayList<Repo>) {
        list.let {
            val searchResultAdapter = RepoAdapter(activity!!)
            searchResultAdapter.data = list
            rvData.adapter = searchResultAdapter
            searchResultAdapter.addItemClickListener { post ->
                Navigation.createNavigateOnClickListener(R.id.repoDetailFragment, null)
                viewPostDetail(post)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateDetail(owner: Owner) {
        tvFullName.text = owner.login
        tvFollowFollowers.text = owner.followers + " Followers | " + owner.following + " Following"
        tvInfo.text = "${owner.company} ${owner.location}"
        ivAvtar.glide(activity!!, owner.avatar_url!!, 30)
    }

    private fun showRepoLoading(isLoading: Boolean) {
        shimmerRepo.setVisiblity(isLoading)
    }

    private fun showOwnerLoading(isLoading: Boolean) {
        shimmerOwner.setVisiblity(isLoading)
    }

    private fun viewPostDetail(repo: Repo) {
        RepositoryDetailFragment.start(navController, repo)
    }

}
