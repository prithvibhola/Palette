package io.palette.utility

import android.arch.paging.PagedListAdapter
import android.content.Context
import android.support.annotation.StringRes
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import io.palette.R
import io.palette.data.models.Response
import io.palette.utility.extentions.inflate
import io.palette.utility.extentions.visible
import kotlinx.android.synthetic.main.layout_network_state.view.*

abstract class PaginatedAdapter<T>(
        val context: Context,
        @StringRes val loadingTitleRes: Int,
        @StringRes val errorTitleRes: Int,
        private val retryCallback: () -> Unit
) : PagedListAdapter<T, PaginatedAdapter<T>.BaseViewHolder>(
        object : DiffUtil.ItemCallback<T>() {
            override fun areItemsTheSame(oldItem: T, newItem: T): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: T, newItem: T): Boolean {
                return oldItem == newItem
            }
        }
) {

    private var response: Response<T>? = null

    enum class Type { ITEM, NETWORK }

    abstract fun onCreateItemViewHolder(parent: ViewGroup): PaginatedAdapter<T>.BaseViewHolder

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder = when (viewType) {
        Type.ITEM.ordinal -> onCreateItemViewHolder(parent)
        Type.NETWORK.ordinal -> NetworkStateViewHolder(parent.inflate(R.layout.layout_network_state), retryCallback)
        else -> onCreateItemViewHolder(parent)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (getItemViewType(position)) {
            Type.ITEM.ordinal -> holder.bind(getItem(position)!!)
            Type.NETWORK.ordinal -> holder.bindNetwork(response)
        }
    }

    override fun getItemViewType(position: Int) = when {
        hasExtraRow() && position == itemCount - 1 -> Type.NETWORK.ordinal
        else -> Type.ITEM.ordinal
    }

    override fun getItemCount() = super.getItemCount() + if (hasExtraRow()) 1 else 0

    private fun hasExtraRow() = response != null && response != Response.ViewState.SUCCESS

    fun setNetworkState(newResponse: Response<T>?) {
        if (currentList != null) {
            if (currentList?.size != 0) {
                val previousState = this.response
                val hadExtraRow = hasExtraRow()
                this.response = newResponse
                val hasExtraRow = hasExtraRow()
                if (hadExtraRow != hasExtraRow) {
                    if (hadExtraRow) {
                        notifyItemRemoved(super.getItemCount())
                    } else {
                        notifyItemInserted(super.getItemCount())
                    }
                } else if (hasExtraRow && previousState !== newResponse) {
                    notifyItemChanged(itemCount - 1)
                }
            }
        }
    }

    inner class NetworkStateViewHolder(val view: View, private val retryCallback: () -> Unit) : BaseViewHolder(view) {

        init {
            itemView.btnRetry.setOnClickListener { retryCallback() }
        }

        override fun bindNetwork(response: Response<T>?) {
            super.bindNetwork(response)
            itemView.btnRetry.visible = response?.status == Response.ViewState.ERROR
            itemView.pbLoading.visible = response?.status == Response.ViewState.LOADING
            if (response?.status == Response.ViewState.LOADING) itemView.tvMessage.text = context.getString(loadingTitleRes)
            if (response?.status == Response.ViewState.ERROR) itemView.tvMessage.text = context.getString(errorTitleRes)
        }
    }

    abstract inner class BaseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun bind(item: T) {}
        open fun bindNetwork(response: Response<T>?) {}
    }
}