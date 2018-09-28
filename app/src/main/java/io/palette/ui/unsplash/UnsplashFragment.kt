package io.palette.ui.unsplash

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v4.content.ContextCompat
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.*
import io.palette.R
import io.palette.data.models.Response
import io.palette.data.models.Unsplash
import io.palette.di.FragmentScoped
import io.palette.ui.base.BaseFragment
import io.palette.ui.detail.DetailActivity
import io.palette.utility.extentions.getViewModel
import io.palette.utility.extentions.observe
import io.palette.utility.extentions.toast
import io.palette.utility.extentions.withDelay
import io.palette.utility.preference.PreferenceUtility
import kotlinx.android.synthetic.main.fragment_unsplash.*
import javax.inject.Inject

@FragmentScoped
class UnsplashFragment @Inject constructor() : BaseFragment(), UnsplashAdapter.Callback {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var preference: PreferenceUtility

    private lateinit var viewModel: UnsplashViewModel
    private lateinit var mAdapter: UnsplashAdapter

    companion object {
        fun newInstance() = UnsplashFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_unsplash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel(UnsplashViewModel::class.java, viewModelFactory)
        viewModel.getLikedPalettes()

        mAdapter = UnsplashAdapter(requireContext(), this) { viewModel.retry() }
        rvUnsplash.apply {
            layoutManager = StaggeredGridLayoutManager(preference.prefUnsplashStaggered, StaggeredGridLayoutManager.VERTICAL)
            adapter = mAdapter
        }

        observe(viewModel.unsplash) { mAdapter.submitList(it) }
        observe(viewModel.getNetworkState()) { mAdapter.setNetworkState(it) }
        observe(viewModel.getRefreshState()) {
            it ?: return@observe
            when (it.status) {
                Response.Status.LOADING -> screenState.showLoading()
                Response.Status.SUCCESS -> {
                    withDelay(200) { swipeRefresh?.isRefreshing = false }
                    it.data ?: return@observe
                    when (it.data.isEmpty()) {
                        true -> screenState.showEmpty(R.drawable.ic_hourglass_empty_black_24dp,
                                "No data.",
                                "No data from unsplash for now.")
                        false -> screenState.showContent()
                    }
                }
                Response.Status.ERROR -> {
                    withDelay(200) { swipeRefresh?.isRefreshing = false }
                    screenState.showError(R.drawable.ic_error_outline_black_24dp,
                            "Couldn't get data!!",
                            "Unable to get unsplash images. Please try again.",
                            "Retry") { viewModel.retry() }
                }
            }
        }
        observe(viewModel.likedPalettes) {
            it ?: return@observe
            when (it.status) {
                Response.Status.LOADING -> {
                }
                Response.Status.SUCCESS -> {
                    it.data ?: return@observe
                    mAdapter.likedPalettes = it.data
                }
                Response.Status.ERROR -> {
                }
            }
        }

        swipeRefresh.setOnRefreshListener {
            viewModel.refresh()
            viewModel.retry()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (menu == null || inflater == null) return
        inflater.inflate(R.menu.menu_unsplash, menu)
        menu.findItem(R.id.action_staggered)?.let {
            it.isVisible = true
            it.icon = ContextCompat.getDrawable(requireContext(), if (preference.prefUnsplashStaggered == 1) R.drawable.ic_view_agenda_black_24dp else R.drawable.ic_view_compact_black_24dp)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_staggered -> {
                val invert = preference.prefUnsplashStaggered == 1
                preference.prefUnsplashStaggered = if (invert) 2 else 1
                item.icon = ContextCompat.getDrawable(requireContext(), if (invert) R.drawable.ic_view_compact_black_24dp else R.drawable.ic_view_agenda_black_24dp)
                rvUnsplash.layoutManager = StaggeredGridLayoutManager(preference.prefUnsplashStaggered, StaggeredGridLayoutManager.VERTICAL)
                mAdapter.notifyDataSetChanged()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun openDetail(view: View, unsplash: Unsplash, isLiked: Boolean) {
        startActivity(DetailActivity.newInstance(requireContext(), unsplash, isLiked, true, false),
                ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity(), view, getString(R.string.transition_image_unsplash)).toBundle())
    }
}