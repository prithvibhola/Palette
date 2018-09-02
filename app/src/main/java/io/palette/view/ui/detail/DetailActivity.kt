package io.palette.view.ui.detail

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import io.palette.R
import io.palette.data.models.Response
import io.palette.utility.extentions.getViewModel
import io.palette.utility.extentions.toast
import io.palette.view.ui.base.BaseActivity
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

        viewModel = getViewModel(DetailViewModel::class.java, viewModelFactory)
        viewModel.palette.observe(this, Observer {
            when (it?.status) {
                Response.ViewState.LOADING -> TODO()
                Response.ViewState.SUCCESS -> mAdapter.palette = it.data!!
                Response.ViewState.ERROR -> TODO()
                null -> TODO()
            }
        })
        viewModel.shareUri.observe(this, Observer {
            when (it?.status) {
                Response.ViewState.LOADING -> TODO()
                Response.ViewState.SUCCESS -> {
                    startActivity(Intent.createChooser(Intent(Intent.ACTION_SEND).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK
                        type = "image/*"
                        putExtra(Intent.EXTRA_STREAM, it.data)
                        putExtra(Intent.EXTRA_SUBJECT, "Palette")
                    }, "Share news using"))
                }
                Response.ViewState.ERROR -> toast("Error sharing palette. Please try again.")
                null -> toast("Some unexpected error occurred")
            }
        })
        viewModel.savePalette.observe(this, Observer {
            when (it?.status) {
                Response.ViewState.LOADING -> TODO()
                Response.ViewState.SUCCESS -> toast("Image saved successfully")
                Response.ViewState.ERROR -> toast("Couldn't save image. Please try again.")
                null -> toast("Some unexpected error occurred")
            }
        })

        Glide.with(this)
                .asBitmap()
                .load(image)
                .listener(object : RequestListener<Bitmap> {
                    override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                        resource?.let {
                            ivImage.setImageBitmap(resource)
                            viewModel.generatePalette(resource)
                            bitmap = resource
                        }
                        return true
                    }

                    override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                        Timber.e(e, "Glide error occurred!!")
                        return true
                    }
                })
                .into(ivImage)

        ivImage.setOnClickListener {
            viewModel.savePalette(rvPalette, false, bitmap!!)
        }
    }
}