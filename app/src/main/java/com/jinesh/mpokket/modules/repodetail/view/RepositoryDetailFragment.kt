package com.jinesh.mpokket.modules.repodetail.view

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
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
import com.jinesh.mpokket.modules.contributor.view.ContributorDetailFragment
import com.jinesh.mpokket.modules.repodetail.viewmodel.OwnerViewModel
import kotlinx.android.synthetic.main.fragment_repo_detail.*


class RepositoryDetailFragment : BaseFragment() {

    companion object {
        const val ARG_REPO = "ARG_REPO"
        fun start(navController: NavController, item: Repo) {
            val bundle = bundleOf(ARG_REPO to item)
            navController.navigate(R.id.action_search_detail, bundle)
        }
    }

    private val viewModel: OwnerViewModel by activityViewModels {
        viewModelFactory
    }

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.repoObject = this.arguments!!.getSerializable(ARG_REPO) as Repo?
        observer()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_repo_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        initData()
    }

    private fun initData() {
        showLoading(true)
        updateDetail(repo = viewModel.repoObject!!)
        viewModel.getRepoContributor()
    }

    private fun observer() {
        viewModel.observePostsLiveData().observe(this, Observer { result ->
            showLoading(false)
            result?.let {
                val searchResultAdapter = OwnerAdapter(activity!!, it!!)
                rvData.adapter = searchResultAdapter
                searchResultAdapter.addItemClickListener { post ->
                    Navigation.createNavigateOnClickListener(R.id.contributorDetailFragment, null)
                    viewPostDetail(post)
                }
            }
        })

        viewModel.observeContributorNotFound().observe(this, Observer { result ->
            showLoading(false)
            tvContributorTitle.setVisiblity(false)
        })
    }

    private fun viewPostDetail(owner: Owner) {
        ContributorDetailFragment.start(navController, owner)
    }

    private fun updateDetail(repo: Repo) {
        tvFullName.text = repo.full_name
        tvName.text = repo.owner!!.login
        tvDescription.text = repo.description
        tvUrls.text = repo.html_url
        ivAvtar.glide(activity!!, repo.owner!!.avatar_url!!, 30)
        tvUrls.setOnClickListener {
            openLinkInBrowser(repo.html_url)
        }
    }

    private fun openLinkInBrowser(url: String?){
        url?.let{
            val builder = CustomTabsIntent.Builder()
            val customTabsIntent = builder.build()
            customTabsIntent.launchUrl(activity!!, Uri.parse(url))
        }
    }

    private fun showLoading(isLoading: Boolean) {
        incShimmer.setVisiblity(isLoading)
    }

}
