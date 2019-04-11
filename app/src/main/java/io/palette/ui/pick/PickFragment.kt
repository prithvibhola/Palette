package io.palette.ui.pick

import android.Manifest
import android.arch.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import com.github.javiersantos.appupdater.AppUpdaterUtils
import com.github.javiersantos.appupdater.enums.AppUpdaterError
import com.github.javiersantos.appupdater.objects.Update
import io.palette.R
import io.palette.data.models.Response
import io.palette.data.models.Source
import io.palette.data.models.Unsplash
import io.palette.di.FragmentScoped
import io.palette.ui.about.AboutActivity
import io.palette.ui.base.BaseFragment
import io.palette.ui.detail.DetailActivity
import io.palette.utility.extentions.getViewModel
import io.palette.utility.extentions.observe
import io.palette.utility.extentions.snackBar
import kotlinx.android.synthetic.main.fragment_pick.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import javax.inject.Inject
import android.content.Intent
import android.net.Uri
import io.palette.utility.extentions.visible
import timber.log.Timber

@FragmentScoped
@RuntimePermissions
class PickFragment @Inject constructor() : BaseFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

//    lateinit var appUpdaterUtils: AppUpdaterUtils

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

//        appUpdaterUtils = AppUpdaterUtils(requireContext())

        btnCamera.setOnClickListener { pickFromCameraWithPermissionCheck() }
        btnGallery.setOnClickListener { pickFromGalleryWithPermissionCheck() }

        observe(viewModel.image) {
            it ?: return@observe
            when (it.status) {
                Response.Status.LOADING -> {
                }
                Response.Status.SUCCESS -> startActivity(DetailActivity.newInstance(requireContext(), Unsplash.from(it.data.toString()), false, false, false))
                Response.Status.ERROR -> rootLayout.snackBar(R.string.error_getting_image)
            }
        }

//        appUpdaterUtils.withListener(object : AppUpdaterUtils.UpdateListener {
//            override fun onSuccess(update: Update, isUpdateAvailable: Boolean) {
//                cardAppUpdate.visible = isUpdateAvailable
//            }
//
//            override fun onFailed(error: AppUpdaterError?) {
//                Timber.e(error.toString(), "Error in checking app update")
//            }
//        }).start()

//        cardAppUpdate.setOnClickListener {
//            val appPackageName = requireContext().packageName
//            try {
//                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
//            } catch (e: android.content.ActivityNotFoundException) {
//                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")))
//            }
//        }

        ivCancel.setOnClickListener { cardNotify.visible = false }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_pick, menu)
        menu.findItem(R.id.action_info)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_info -> {
                startActivity(AboutActivity.newInstance(requireContext()))
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onDestroy() {
//        appUpdaterUtils.stop()
        super.onDestroy()
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