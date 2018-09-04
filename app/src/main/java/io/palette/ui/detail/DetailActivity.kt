package io.palette.ui.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import io.palette.R
import io.palette.data.models.Response
import io.palette.ui.base.BaseActivity
import io.palette.utility.extentions.getViewModel
import io.palette.utility.extentions.listen
import io.palette.utility.extentions.observe
import io.palette.utility.extentions.toast
import kotlinx.android.synthetic.main.activity_detail.*
import timber.log.Timber
import javax.inject.Inject

class DetailActivity @Inject constructor() : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    lateinit var image: String
    lateinit var userName: String
    lateinit var mAdapter: DetailAdapter
    lateinit var viewModel: DetailViewModel

    var bitmap: Bitmap? = null

    companion object {
        const val ARG_IMAGE = "ARG_IMAGE"
        const val ARG_USER_NAME = "ARG_USER_NAME"

        fun newInstance(context: Context, image: String, userName: String = "Palette") = Intent(context, DetailActivity::class.java).apply {
            putExtra(ARG_IMAGE, image)
            putExtra(ARG_USER_NAME, userName)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        image = intent.getStringExtra(ARG_IMAGE)
        userName = intent.getStringExtra(ARG_USER_NAME)

        mAdapter = DetailAdapter(userName)
        rvPalette.apply {
            layoutManager = LinearLayoutManager(this@DetailActivity)
            adapter = mAdapter
        }

        ivImage.setOnClickListener { viewModel.savePalette(rvPalette, false, bitmap!!) }

        viewModel = getViewModel(DetailViewModel::class.java, viewModelFactory)
        observe(viewModel.palette) {
            it ?: return@observe
            when (it.status) {
                Response.Status.LOADING -> activityStatus.showLoading()
                Response.Status.SUCCESS -> {
                    it.data ?: return@observe
                    mAdapter.palette = it.data
                    activityStatus.showContent()
                }
                Response.Status.ERROR -> TODO()
            }
        }

        observe(viewModel.shareUri) {
            it ?: return@observe
            when (it.status) {
                Response.Status.LOADING -> TODO()
                Response.Status.SUCCESS -> {
                    startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        type = "image/*"
                        putExtra(Intent.EXTRA_STREAM, it.data)
                        putExtra(Intent.EXTRA_SUBJECT, "Palette")
                    }, "Share news using"))
                }
                Response.Status.ERROR -> toast("Error sharing palette. Please try again.")
            }
        }

        viewModel.savePalette.observe(this, Observer {
            it ?: return@Observer
            when (it.status) {
                Response.Status.LOADING -> TODO()
                Response.Status.SUCCESS -> toast("Image saved successfully")
                Response.Status.ERROR -> toast("Couldn't save image. Please try again.")
            }
        })

        Glide.with(this)
                .asBitmap()
                .load(image)
                .listen(
                        resourceReady = { resource, _, _, _, _ ->
                            resource?.let {
                                ivImage.setImageBitmap(resource)
                                viewModel.generatePalette(resource)
                                bitmap = resource
                            }
                        },
                        loadFailed = { e, _, _, _ ->
                            Timber.e(e, "Glide error occurred!!")
                        }
                )
                .into(ivImage)
    }
}