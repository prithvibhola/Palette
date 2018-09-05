package io.palette.ui.pick

import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.palette.R
import io.palette.data.models.Response
import io.palette.data.models.Source
import io.palette.di.FragmentScoped
import io.palette.ui.base.BaseFragment
import io.palette.ui.detail.DetailActivity
import io.palette.utility.extentions.getViewModel
import io.palette.utility.extentions.observe
import kotlinx.android.synthetic.main.fragment_pick.*
import javax.inject.Inject

@FragmentScoped
class PickFragment @Inject constructor() : BaseFragment() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: PickViewModel

    companion object {
        fun newInstance() = PickFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) =
            inflater.inflate(R.layout.fragment_pick, container, false)!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel(PickViewModel::class.java, viewModelFactory)

        btnCamera.setOnClickListener { viewModel.openImagePicker(Source.CAMERA) }
        btnGallery.setOnClickListener { viewModel.openImagePicker(Source.GALLERY) }

        observe(viewModel.image) {
            it ?: return@observe
            when (it.status) {
                Response.Status.LOADING -> TODO()
                Response.Status.SUCCESS -> startActivity(DetailActivity.newInstance(requireContext(), it.data.toString()))
                Response.Status.ERROR -> TODO()
            }
        }
    }
}