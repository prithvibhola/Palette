package io.palette.view.ui.unsplash

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.flexbox.*
import io.palette.R
import io.palette.data.models.Response
import io.palette.data.models.Unsplash
import io.palette.di.FragmentScoped
import io.palette.utility.extentions.getViewModel
import io.palette.view.ui.base.BaseFragment
import io.palette.viewmodel.UnsplashViewModel
import kotlinx.android.synthetic.main.fragment_unsplash.*
import javax.inject.Inject

@FragmentScoped
class UnsplashFragment @Inject constructor() : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var mAdapter: UnsplashAdapter
    lateinit var viewModel: UnsplashViewModel

    companion object {
        fun newInstance() = UnsplashFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_unsplash, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = getViewModel(UnsplashViewModel::class.java, viewModelFactory)
        setUpRecyclerView()
        viewModel.unsplash.observe(this, Observer { mAdapter.submitList(it) })
        viewModel.getNetworkState().observe(this, Observer<Response<Unsplash>> { mAdapter.setNetworkState(it) })
        viewModel.getRefreshState().observe(this, Observer { setInitialLoadingState(it) })
    }

    private fun setUpRecyclerView() {
        mAdapter = UnsplashAdapter(requireContext(), { viewModel.retry() })
        rvUnsplash.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
            adapter = mAdapter
        }
    }

    private fun setInitialLoadingState(state: Response<Unsplash>?) {
        when (state?.status) {
            Response.ViewState.SUCCESS -> {
            }
            Response.ViewState.LOADING -> {
            }
            Response.ViewState.ERROR -> {
            }
        }
    }
}