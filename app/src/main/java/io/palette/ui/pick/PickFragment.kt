package io.palette.ui.pick

import android.Manifest
import android.arch.lifecycle.ViewModelProvider
import android.content.Intent
import android.os.Bundle
import android.view.*
import io.palette.R
import io.palette.data.models.Response
import io.palette.data.models.Source
import io.palette.data.models.Unsplash
import io.palette.data.models.Urls
import io.palette.di.FragmentScoped
import io.palette.ui.about.AboutActivity
import io.palette.ui.base.BaseFragment
import io.palette.ui.detail.DetailActivity
import io.palette.utility.extentions.getViewModel
import io.palette.utility.extentions.observe
import kotlinx.android.synthetic.main.fragment_pick.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import javax.inject.Inject

@FragmentScoped
@RuntimePermissions
class PickFragment @Inject constructor() : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var viewModel: PickViewModel

    companion object {
        fun newInstance() = PickFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        setHasOptionsMenu(true)
        return inflater.inflate(R.layout.fragment_pick, container, false)!!
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = getViewModel(PickViewModel::class.java, viewModelFactory)

        btnCamera.setOnClickListener { pickFromCameraWithPermissionCheck() }
        btnGallery.setOnClickListener { pickFromGalleryWithPermissionCheck() }

        observe(viewModel.image) {
            it ?: return@observe
            when (it.status) {
                Response.Status.LOADING -> TODO()
                Response.Status.SUCCESS -> startActivity(DetailActivity.newInstance(requireContext(),
                        Unsplash(
                                id = "",
                                createdAt = "",
                                updatedAt = "",
                                width = 2040L,
                                height = 2900L,
                                color = "",
                                description = "",
                                urls = Urls(it.data.toString(),
                                        it.data.toString(),
                                        it.data.toString(),
                                        it.data.toString(),
                                        it.data.toString()),
                                links = null,
                                user = null
                        ), false, false, false))
                Response.Status.ERROR -> TODO()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        if (menu == null || inflater == null) return
        inflater.inflate(R.menu.menu_pick, menu)
        menu.findItem(R.id.action_info)?.let { it.isVisible = true }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_info -> {
                startActivity(Intent(requireContext(), AboutActivity::class.java))
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun pickFromCamera() {
        viewModel.openImagePicker(requireFragmentManager(), Source.CAMERA)
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun pickFromGallery() {
        viewModel.openImagePicker(requireFragmentManager(), Source.GALLERY)
    }
}