package io.palette.ui.profile

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import io.palette.R
import io.palette.data.models.Unsplash
import io.palette.ui.base.BaseViewHolder
import io.palette.ui.detail.DetailActivity
import io.palette.utility.extentions.getRandom
import io.palette.utility.extentions.inflate
import io.palette.utility.extentions.maintainAspectRatio
import kotlinx.android.synthetic.main.layout_unsplash.view.*

class ProfileAdapter(
        val context: Context,
        val callBack: Callback
) : RecyclerView.Adapter<BaseViewHolder>() {

    var palettes = listOf<Unsplash>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(parent.inflate(R.layout.layout_unsplash))

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) = holder.bind()

    override fun getItemCount() = palettes.size

    inner class ViewHolder(itemView: View) : BaseViewHolder(itemView) {

        init {
            itemView.ivImage.setOnClickListener {
                if (adapterPosition == RecyclerView.NO_POSITION) return@setOnClickListener
                callBack.openDetail(itemView.ivImage, palettes[adapterPosition])
            }
        }

        override fun bind() {
            val palette = palettes[adapterPosition]
            Glide.with(context)
                    .setDefaultRequestOptions(RequestOptions().apply {
                        placeholder(listOf(R.color.colorBlack, R.color.colorBlackTwo, R.color.colorBlackThree).getRandom())
                        error(R.color.colorBlackTwo)
                    })
                    .asBitmap()
                    .load(palette.urls!!.regular)
                    .into(itemView.ivImage)
            itemView.ivImage.maintainAspectRatio(palette.width, palette.height)
        }
    }

    interface Callback {
        fun openDetail(view: View, unsplash: Unsplash)
    }
}