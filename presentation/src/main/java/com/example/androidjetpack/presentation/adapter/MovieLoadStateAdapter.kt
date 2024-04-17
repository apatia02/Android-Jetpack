package com.example.androidjetpack.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidjetpack.presentation.databinding.LayoutFooterPaginationBinding

class MovieLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<MovieLoadStateAdapter.FooterViewHolder>() {

    override fun onBindViewHolder(holder: FooterViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): FooterViewHolder {
        return FooterViewHolder.create(parent, retry)
    }

    class FooterViewHolder private constructor(
        private val binding: LayoutFooterPaginationBinding,
        private val retryClick: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun create(parent: ViewGroup, retryClick: () -> Unit): FooterViewHolder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = LayoutFooterPaginationBinding.inflate(inflater, parent, false)
                return FooterViewHolder(binding, retryClick)
            }
        }

        fun bind(loadState: LoadState) = with(binding) {
            loadingState.isGone = loadState !is LoadState.Loading
            errorState.isGone = loadState !is LoadState.Error
            reloadIv.setOnClickListener { retryClick() }
        }
    }
}