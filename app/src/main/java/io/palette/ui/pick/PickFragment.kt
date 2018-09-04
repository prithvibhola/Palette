package io.palette.ui.pick

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.palette.R
import io.palette.data.models.Response
import io.palette.data.models.Sources
import io.palette.di.FragmentScoped
import io.palette.utility.extentions.getViewModel
import io.palette.ui.base.BaseFragment
import io.palette.ui.detail.DetailActivity
import io.palette.viewmodel.PickViewModel
import kotlinx.android.synthetic.main.fragment_pick.*
import timber.log.Timber
import javax.inject.Inject

@FragmentScoped
class PickFragment @Inject constructor() : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: PickViewModel

    companion object {
        fun newInstance() = PickFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_pick, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel(PickViewModel::class.java, viewModelFactory)

        btnCamera.setOnClickListener { viewModel.openImagePicker(Sources.CAMERA) }
        btnGallery.setOnClickListener { viewModel.openImagePicker(Sources.GALLERY) }

        viewModel.image.observe(this, Observer {
            when (it?.status) {
                Response.Status.LOADING -> TODO()
                Response.Status.SUCCESS -> startActivity(DetailActivity.newInstance(requireContext(), it.data.toString()))
                Response.Status.ERROR -> TODO()
                null -> TODO()
            }
        })
    }
}