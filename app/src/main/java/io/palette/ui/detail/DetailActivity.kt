package io.palette.ui.detail

import android.Manifest
import android.animation.ObjectAnimator
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import io.palette.R
import io.palette.data.models.Response
import io.palette.data.models.Unsplash
import io.palette.ui.base.BaseActivity
import io.palette.utility.extentions.*
import io.palette.utility.preference.PreferenceUtility
import kotlinx.android.synthetic.main.activity_detail.*
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions
import timber.log.Timber
import javax.inject.Inject

@RuntimePermissions
class DetailActivity @Inject constructor() : BaseActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject lateinit var preferences: PreferenceUtility

    private lateinit var mAdapter: DetailAdapter
    private lateinit var viewModel: DetailViewModel

    private var bitmap: Bitmap? = null

    private lateinit var unsplash: Unsplash
    private var isLiked: Boolean = false
    private var isUnsplash: Boolean = false
    private var fromProfile: Boolean = false

    private var likeStatusChanged: Boolean = false

    private var pulseAnimator: ObjectAnimator? = null
    private var downAnimator: ObjectAnimator? = null

    companion object {
        const val ARG_UNSPLASH = "ARG_UNSPLASH"
        const val ARG_IS_LIKED = "ARG_IS_LIKED"
        const val ARG_IS_UNSPLASH = "ARG_IS_UNSPLASH"
        const val ARG_FROM_PROFILE = "ARG_FROM_PROFILE"

        fun newInstance(context: Context, unsplash: Unsplash, isLiked: Boolean, isUnsplash: Boolean, fromProfile: Boolean) = Intent(context, DetailActivity::class.java).apply {
            putExtra(ARG_UNSPLASH, unsplash)
            putExtra(ARG_IS_LIKED, isLiked)
            putExtra(ARG_IS_UNSPLASH, isUnsplash)
            putExtra(ARG_FROM_PROFILE, fromProfile)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        unsplash = intent.getParcelableExtra(ARG_UNSPLASH)
        isLiked = intent.getBooleanExtra(ARG_IS_LIKED, false)
        isUnsplash = intent.getBooleanExtra(ARG_IS_UNSPLASH, false)
        fromProfile = intent.getBooleanExtra(ARG_FROM_PROFILE, false)

        viewModel = getViewModel(DetailViewModel::class.java, viewModelFactory)

        mAdapter = DetailAdapter(this, unsplash.user?.name
                ?: getString(R.string.app_name), unsplash.updatedAt.defaultDate(), isLiked, preferences.prefShowRGB, isUnsplash).apply {
            savePalette = { savePaletteWithPermissionCheck() }
            sharePalette = { sharePaletteWithPermissionCheck() }
            likePalette = {
                pulseAnimator = it
                viewModel.likeUnlikePalette(unsplash, isLiked)
            }
            setWallpaper = {
                downAnimator = it
                setWallpaperWithPermissionCheck()
            }
        }
        rvPalette.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = mAdapter
            animateAlpha()
        }

        observe(viewModel.palette) {
            it ?: return@observe
            it.data ?: return@observe
            if (it.status == Response.Status.SUCCESS) mAdapter.palette = it.data
        }

        observe(viewModel.shareUri) {
            it ?: return@observe
            when (it.status) {
                Response.Status.LOADING -> rootLayout.snackBar(R.string.please_wait)
                Response.Status.SUCCESS -> {
                    startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        type = "image/*"
                        putExtra(Intent.EXTRA_STREAM, it.data)
                        putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
                    }, getString(R.string.share_palette_using)))
                }
                Response.Status.ERROR -> rootLayout.snackBar(R.string.error_sharing_palette)
            }
        }

        observe(viewModel.savePalette) {
            it ?: return@observe
            when (it.status) {
                Response.Status.LOADING -> rootLayout.snackBar(R.string.please_wait)
                Response.Status.SUCCESS -> rootLayout.snackBar(R.string.palette_saved)
                Response.Status.ERROR -> rootLayout.snackBar(R.string.error_palette_saved)
            }
        }

        observe(viewModel.likeUnlikePalette) {
            it ?: return@observe
            when (it.status) {
                Response.Status.LOADING -> rootLayout.snackBar(R.string.please_wait)
                Response.Status.SUCCESS -> {
                    pulseAnimator?.end()
                    if (it.data == null) {
                        rootLayout.snackBar(R.string.user_not_logged_in)
                    } else {
                        isLiked = it.data
                        mAdapter.isLiked = isLiked
                        likeStatusChanged = true
                    }
                }
                Response.Status.ERROR -> {
                    pulseAnimator?.end()
                    rootLayout.snackBar(R.string.error_liking_palette)
                }
            }
        }

        observe(viewModel.saveWallpaper) {
            it ?: return@observe
            when (it.status) {
                Response.Status.LOADING -> {
                }
                Response.Status.SUCCESS -> {
                    it.data ?: return@observe
                    downAnimator?.end()
                    mAdapter.showWallIcon = true
                    startActivity(Intent.createChooser(Intent(Intent.ACTION_ATTACH_DATA).apply {
                        addCategory(Intent.CATEGORY_DEFAULT)
                        setDataAndType(it.data, "image/*")
                        putExtra("mimeType", "image/*")
                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                    }, getString(R.string.set_wallpaper)))
                }
                Response.Status.ERROR -> {
                    downAnimator?.end()
                    mAdapter.showWallIcon = true
                    rootLayout.snackBar(R.string.error_save_wallpaper)
                }
            }
        }

        Glide.with(this)
                .asBitmap()
                .load(unsplash.urls?.regular)
                .listen(
                        resourceReady = { resource, _, _, _, _ ->
                            resource?.let {
                                ivImage.setImageBitmap(resource)
                                viewModel.generatePalette(resource)
                                bitmap = resource
                            }
                        },
                        loadFailed = { e, _, _, _ -> Timber.e(e, "Glide error occurred!!") }
                )
                .into(ivImage)
        ivImage.maintainAspectRatio(unsplash.width, unsplash.height)

        ivBack.setOnClickListener { onBackPressed() }
    }

    override fun onBackPressed() {
        when {
            !fromProfile -> super.onBackPressed()
            likeStatusChanged -> finish()
            else -> super.onBackPressed()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun sharePalette() = viewModel.savePalette(rvPalette, true, bitmap!!)

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun savePalette() = viewModel.savePalette(rvPalette, false, bitmap!!)

    @NeedsPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun setWallpaper() =
            Glide.with(this)
                    .asBitmap()
                    .load(unsplash.urls?.full)
                    .loadInto(
                            resourceReady = { resource, _ -> viewModel.saveWallpaper(resource) }
                    )
}