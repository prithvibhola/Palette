package io.palette.view.ui.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.palette.R
import io.palette.data.models.GeneratedPalette
import io.palette.utility.extentions.inflate
import kotlinx.android.synthetic.main.layout_detail.view.*
import kotlinx.android.synthetic.main.layout_detail_info.view.*

@SuppressLint("SetTextI18n")
class DetailAdapter(
        val userName: String
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var palette: List<GeneratedPalette> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var sharePalette: ((itemView: View) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = when (viewType) {
        R.layout.layout_detail_info -> ViewHolderDetailInfo(parent.inflate(R.layout.layout_detail_info))
        else -> ViewHolderDetail(parent.inflate(R.layout.layout_detail))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) = when (position) {
        0 -> (holder as ViewHolderDetailInfo).bind(palette[position])
        else -> (holder as ViewHolderDetail).bind(palette[position - 1])
    }

    override fun getItemCount() = if (palette.isNotEmpty()) palette.size else 0

    override fun getItemViewType(position: Int) = when (position) {
        0 -> R.layout.layout_detail_info
        else -> R.layout.layout_detail
    }

    inner class ViewHolderDetailInfo(item: View) : RecyclerView.ViewHolder(item) {

        fun bind(palette: GeneratedPalette) {
            itemView.apply {
                infoRootLayout.setBackgroundColor(Color.parseColor("#${palette.hexCode}"))
                tvPhotographerName.text = userName
            }
        }
    }

    inner class ViewHolderDetail(item: View) : RecyclerView.ViewHolder(item) {

        init {
            itemView.tvHexCode.setOnClickListener {
                sharePalette?.invoke(itemView.rootLayout)
            }
        }

        fun bind(palette: GeneratedPalette) {
            itemView.apply {
                rootLayout.setBackgroundColor(Color.parseColor("#${palette.hexCode}"))
                tvHexCode.text = "#${palette.hexCode.substring(2)}"
                tvPopulation.text = palette.population.toString()
            }
        }
    }
}
